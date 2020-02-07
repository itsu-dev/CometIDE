package dev.itsu.cometide.ui.editor.text

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.editor.AbstractEditor
import org.fxmisc.richtext.StyleClassedTextArea
import org.fxmisc.richtext.model.StyleSpans

class TextEditorImpl(treeItemData: TreeItemData) : AbstractEditor(treeItemData, "") {

    override fun setUp(codeArea: StyleClassedTextArea) = disableHighlighting()

    override fun computeHighlighting(text: String): StyleSpans<Collection<String>>? = null

}