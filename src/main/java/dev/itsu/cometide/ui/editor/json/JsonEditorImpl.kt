package dev.itsu.cometide.ui.editor.json

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.util.JsonParserDelegate
import dev.itsu.cometide.editor.lang.java.JavaKeywordMarker
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.editor.AbstractEditor
import org.fxmisc.richtext.StyleClassedTextArea
import org.fxmisc.richtext.model.StyleSpans

class JsonEditorImpl(treeItemData: TreeItemData) : AbstractEditor(treeItemData, "json") {

    override fun setUp(codeArea: StyleClassedTextArea) {

    }

    override fun computeHighlighting(text: String): StyleSpans<Collection<String>>? {
        return JavaKeywordMarker.mark("")
    }

    override fun onAppliedHighlighting() {
        var offset = 0
        val parser = JsonFactory().createParser(codeArea.text)
        while(parser.nextToken() != JsonToken.END_OBJECT) {
            //println("" + parser.textOffset + ":" + parser.currentName + ":" + parser.currentToken)
            when (parser.currentToken) {
                JsonToken.FIELD_NAME -> {
                    codeArea.setStyle(
                            parser.textOffset,
                            parser.currentName.length + parser.textOffset,
                            setOf("-fx-fill: #98C379;")
                    )
                }
            }
        }
    }

}