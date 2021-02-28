package dev.itsu.cometide.ui.editor.markdown

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.editor.AbstractEditor
import org.fxmisc.richtext.StyleClassedTextArea
import org.fxmisc.richtext.model.StyleSpan
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder

class MarkdownEditor(val markdownEditorImpl: MarkdownEditorImpl, treeItemData: TreeItemData) : AbstractEditor(treeItemData, "md") {

    override fun setUp(codeArea: StyleClassedTextArea) {

    }

    override fun computeHighlighting(text: String): StyleSpans<Collection<String>>? {
        return StyleSpansBuilder<Collection<String>>().let {
            it.add(emptyList(), 0)
            it.create()
        }
    }

    override fun onAppliedHighlighting() {
        //markdownEditorImpl.onHighlighting()
    }
}