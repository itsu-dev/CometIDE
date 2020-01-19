package dev.itsu.cometide.ui.editor

import dev.itsu.cometide.data.EnvironmentSettings
import dev.itsu.cometide.data.Settings
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.contentbase.tabcontent.ITabContent
import dev.itsu.cometide.util.IOUtils
import javafx.application.Platform
import javafx.concurrent.Task
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.StyleClassedTextArea
import org.fxmisc.richtext.model.StyleSpans
import org.reactfx.Subscription
import java.io.File
import java.time.Duration
import java.util.*
import java.util.concurrent.Executors

abstract class AbstractEditor(treeItemData: TreeItemData, extension: String) : ITabContent {

    val codeArea = StyleClassedTextArea()
    private val virtualizedScrollPane = VirtualizedScrollPane(codeArea)
    private val service = Executors.newSingleThreadExecutor()
    private val cleanupWhenDone: Subscription

    init {
        codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)
        codeArea.id = extension
        cleanupWhenDone = codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask<StyleSpans<Collection<String>>> { this.computeHighlightingAsync() }
                .awaitLatest(codeArea.multiPlainChanges())
                .filterMap<StyleSpans<Collection<String>>> {
                    if (it.isSuccess) {
                        return@filterMap Optional.of(it.get())
                    } else {
                        it.failure.printStackTrace()
                        return@filterMap Optional.empty()
                    }
                }
                .subscribe { highlighting: StyleSpans<Collection<String>> -> this.applyHighlighting(highlighting) }

        setUp(codeArea)

        val css = File(EnvironmentSettings.THEMES_DIRECTORY_PATH + "/" + Settings.getInstance().theme + "/syntax/" + extension + ".css")
        if (css.exists()) {
            UIManager.getInstance().loadCSS(css.toURI().toString())
        } else {
            val url = AbstractEditor::class.java.classLoader.getResource("theme/dark/syntax/$extension.css")
            if (url != null) UIManager.getInstance().loadCSS(url.toExternalForm())
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

    fun applyHighlighting(highlighting: StyleSpans<Collection<String>>) {
        codeArea.setStyleSpans(0, highlighting)
        onAppliedHighlighting()
    }

    fun disableHighlighting() {
        cleanupWhenDone.unsubscribe()
        service.shutdown()
    }

    open fun onAppliedHighlighting() {

    }

    open fun onEditorClose() {

    }

    open fun onClose() {
        onEditorClose()
        service.shutdown()
    }

    override fun getContent(): VirtualizedScrollPane<StyleClassedTextArea> {
        return virtualizedScrollPane
    }

    private fun loadFile(path: String) {
        val thread = Thread(Runnable { Platform.runLater { codeArea.replaceText(IOUtils.readFileFromFile(File(path))) } })
        thread.start()
    }
}