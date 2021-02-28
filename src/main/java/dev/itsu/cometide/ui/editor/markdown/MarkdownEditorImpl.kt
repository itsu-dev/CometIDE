package dev.itsu.cometide.ui.editor.markdown

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.editor.common.EditorComponent
import dev.itsu.cometide.ui.editor.common.EditorVariables
import dev.itsu.cometide.ui.part.tab.TabContent
import dev.itsu.cometide.util.IOUtils
import javafx.geometry.Orientation
import javafx.scene.control.SplitPane
import javafx.scene.layout.StackPane
import javafx.scene.web.WebView

class MarkdownEditorImpl(treeItemData: TreeItemData) : TabContent(treeItemData) {

    private val splitPane = SplitPane()
    private val editor = EditorComponent(treeItemData,
            EditorVariables
                    .create()
                    .setEditorMode("ace/mode/markdown")
    )
    private val webView = WebView()
    private val stackPane = StackPane(webView)

    init {
        splitPane.orientation = Orientation.HORIZONTAL
        splitPane.items.addAll(editor, stackPane)

        webView.engine.isJavaScriptEnabled = true
        
        this.content = splitPane
        editor.setOnLoad {
            webView.engine.loadContent(
                IOUtils.readFileFromInputStream(this.javaClass.classLoader.getResourceAsStream("data/markdown.html")).replace("MARK_DOWN_TEXT", editor.getText())
            )
        }
    }

    /*
    fun onHighlighting() {
        webView.engine.loadContent(
                IOUtils.readFileFromInputStream(this.javaClass.classLoader.getResourceAsStream("data/markdown.html")).replace("MARK_DOWN_TEXT", editor.codeArea.text)
        )
    }

     */

}