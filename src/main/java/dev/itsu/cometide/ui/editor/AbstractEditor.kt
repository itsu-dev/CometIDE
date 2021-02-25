package dev.itsu.cometide.ui.editor

import dev.itsu.cometide.dao.SettingsDao
import dev.itsu.cometide.data.EditorData
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.part.tab.TabContent
import dev.itsu.cometide.util.IOUtils
import javafx.application.Platform
import javafx.concurrent.Task
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.StyleClassedTextArea
import org.fxmisc.richtext.event.MouseOverTextEvent
import org.fxmisc.richtext.model.Paragraph
import org.fxmisc.richtext.model.StyleSpans
import org.reactfx.Subscription
import org.reactfx.value.Var
import java.io.File
import java.time.Duration
import java.util.*
import java.util.concurrent.Executors

abstract class AbstractEditor(treeItemData: TreeItemData, extension: String) : TabContent(treeItemData) {

    val codeArea = StyleClassedTextArea()
    private val virtualizedScrollPane = VirtualizedScrollPane(codeArea)
    private val service = Executors.newSingleThreadExecutor()
    private val cleanupWhenDone: Subscription
    private val textLabel = Label()
    private var zoomLevel = 1.0

    fun setTextLabel(text: String) {
        textLabel.text = text
    }

    init {
        content = BorderPane().also {
            it.center = virtualizedScrollPane
            it.bottom = textLabel
        }

        textLabel.style = "-fx-text-fill: #FFFFFF; -fx-background-color: #0F111A"
        textLabel.prefWidth = Double.MAX_VALUE

        codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)
        codeArea.id = extension
        codeArea.mouseOverTextDelay = Duration.ofSeconds(1)

        cleanupWhenDone = codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask { this.computeHighlightingAsync() }
                .awaitLatest(codeArea.multiPlainChanges())
                .filterMap {
                    if (it.isSuccess) {
                        return@filterMap Optional.of(it.get())
                    } else {
                        it.failure.printStackTrace()
                        return@filterMap Optional.empty<StyleSpans<Collection<String>>>()
                    }
                }
                .subscribe { highlighting: StyleSpans<Collection<String>> -> this.applyHighlighting(highlighting) }

        setUp(codeArea)

        val css = File(SettingsDao.ENVIRONMENT_VARIABLES["THEMES_DIRECTORY_PATH"] + "/" + SettingsDao.THEME + "/syntax/" + extension + ".css")
        if (css.exists()) {
            UIManager.loadCSS(css.toURI().toString())
        } else {
            val url = AbstractEditor::class.java.classLoader.getResource("theme/dark/syntax/$extension.css")
            if (url != null) UIManager.loadCSS(url.toExternalForm())
        }

        codeArea.caretPositionProperty().addListener { _, _, _ ->
            UIManager.getBottomBarController().getDataModel().setCaretPosition("${codeArea.currentParagraph + 1}:${codeArea.caretColumn + 1}")
            onCaretPositionChanged(codeArea.currentParagraph, codeArea.caretColumn)
        }

        codeArea.focusedProperty().addListener { _ ->
            EditorData.currentEditor = this
        }

        codeArea.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN) {
            onMouseOverTextStart(it)
        }

        codeArea.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END) {
            onMouseOverTextEnd(it)
        }

        loadFile(treeItemData.path)
    }

    abstract fun setUp(codeArea: StyleClassedTextArea)
    abstract fun computeHighlighting(text: String): StyleSpans<Collection<String>>?

    private fun computeHighlightingAsync(): Task<StyleSpans<Collection<String>>>? {
        val text = codeArea.text
        val task = object : Task<StyleSpans<Collection<String>>>() {
            override fun call(): StyleSpans<Collection<String>>? {
                return computeHighlighting(text)
            }
        }
        service.execute(task)
        return task
    }

    private fun applyHighlighting(highlighting: StyleSpans<Collection<String>>) {
        codeArea.setStyleSpans(0, highlighting)
        onAppliedHighlighting()
    }

    fun disableHighlighting() {
        cleanupWhenDone.unsubscribe()
        service.shutdown()
    }

    open fun onAppliedHighlighting() {

    }

    open fun onMouseOverTextStart(event: MouseOverTextEvent) {

    }

    open fun onMouseOverTextEnd(event: MouseOverTextEvent) {

    }

    open fun onCaretPositionChanged(paragraph: Int, column: Int) {

    }

    open fun onEditorClose() {

    }

    open fun onClose() {
        onEditorClose()
        service.shutdown()
    }

    private fun loadFile(path: String) {
        val thread = Thread(Runnable { Platform.runLater { codeArea.replaceText(IOUtils.readFileFromFile(File(path))) } })
        thread.start()
    }

    fun undo() {
        codeArea.undo()
    }

    fun redo() {
        codeArea.redo()
    }

    fun copy() {
        codeArea.copy()
    }

    fun paste() {
        codeArea.paste()
    }

    fun cut() {
        codeArea.cut();
    }
}