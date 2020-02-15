package dev.itsu.cometide.ui.editor.markdown

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.part.tab.TabContent
import dev.itsu.cometide.util.IOUtils
import javafx.geometry.Orientation
import javafx.scene.control.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane
import javafx.scene.web.WebView

class MarkdownEditorImpl(treeItemData: TreeItemData) : TabContent(treeItemData) {

    private val splitPane = SplitPane()
    private val editor = MarkdownEditor(this, treeItemData)
    private val webView = WebView()
    private val stackPane = StackPane(webView)

    init {
        splitPane.orientation = Orientation.HORIZONTAL
        splitPane.items.addAll(editor.content, stackPane)

        webView.engine.isJavaScriptEnabled = true
        /*
        splitPane.dividers[0].positionProperty().addListener { _, _, value ->
            stackPane.prefWidth = splitPane.width * value.toDouble()
            webView.zoom = (splitPane.width * value.toDouble()) / webView.maxWidth
        }

         */

        this.content = splitPane
    }

    fun onHighlighting() {
        webView.engine.loadContent(
                IOUtils.readFileFromInputStream(this.javaClass.classLoader.getResourceAsStream("data/markdown.html")).replace("MARK_DOWN_TEXT", editor.codeArea.text)
        )
    }

}