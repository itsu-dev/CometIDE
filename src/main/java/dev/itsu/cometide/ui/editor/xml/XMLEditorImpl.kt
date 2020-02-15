package dev.itsu.cometide.ui.editor.xml

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.editor.AbstractEditor
import org.fxmisc.richtext.StyleClassedTextArea
import org.fxmisc.richtext.event.MouseOverTextEvent
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class XMLEditorImpl(treeItemData: TreeItemData) : AbstractEditor(treeItemData, "xml") {

    private val XML_TAG = Pattern.compile("(?<ELEMENT>(</?\\h*)(\\w+)([^<>]*)(\\h*/?>))"
            + "|(?<COMMENT><!--[^<>]+-->)")

    private val ATTRIBUTES = Pattern.compile("(\\w+\\h*)(=)(\\h*\"[^\"]+\")")

    private val GROUP_OPEN_BRACKET = 2
    private val GROUP_ELEMENT_NAME = 3
    private val GROUP_ATTRIBUTES_SECTION = 4
    private val GROUP_CLOSE_BRACKET = 5
    private val GROUP_ATTRIBUTE_NAME = 1
    private val GROUP_EQUAL_SYMBOL = 2
    private val GROUP_ATTRIBUTE_VALUE = 3

    override fun setUp(codeArea: StyleClassedTextArea) {
        println("ANAL")
    }

    override fun onAppliedHighlighting() {

    }

    override fun computeHighlighting(text: String): StyleSpans<Collection<String>> {
        val matcher: Matcher = XML_TAG.matcher(text)
        var lastKwEnd = 0
        val spansBuilder = StyleSpansBuilder<Collection<String>>()
        while (matcher.find()) {
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd)
            if (matcher.group("COMMENT") != null) {
                spansBuilder.add(Collections.singleton("comment"), matcher.end() - matcher.start())
            } else {
                if (matcher.group("ELEMENT") != null) {
                    val attributesText: String = matcher.group(GROUP_ATTRIBUTES_SECTION)
                    spansBuilder.add(Collections.singleton("tagmark"), matcher.end(GROUP_OPEN_BRACKET) - matcher.start(GROUP_OPEN_BRACKET))
                    spansBuilder.add(Collections.singleton("anytag"), matcher.end(GROUP_ELEMENT_NAME) - matcher.end(GROUP_OPEN_BRACKET))
                    if (attributesText.isNotEmpty()) {
                        lastKwEnd = 0
                        val amatcher: Matcher = ATTRIBUTES.matcher(attributesText)
                        while (amatcher.find()) {
                            spansBuilder.add(Collections.emptyList(), amatcher.start() - lastKwEnd)
                            spansBuilder.add(Collections.singleton("attribute"), amatcher.end(GROUP_ATTRIBUTE_NAME) - amatcher.start(GROUP_ATTRIBUTE_NAME))
                            spansBuilder.add(Collections.singleton("tagmark"), amatcher.end(GROUP_EQUAL_SYMBOL) - amatcher.end(GROUP_ATTRIBUTE_NAME))
                            spansBuilder.add(Collections.singleton("avalue"), amatcher.end(GROUP_ATTRIBUTE_VALUE) - amatcher.end(GROUP_EQUAL_SYMBOL))
                            lastKwEnd = amatcher.end()
                        }
                        if (attributesText.length > lastKwEnd) spansBuilder.add(Collections.emptyList(), attributesText.length - lastKwEnd)
                    }
                    lastKwEnd = matcher.end(GROUP_ATTRIBUTES_SECTION)
                    spansBuilder.add(Collections.singleton("tagmark"), matcher.end(GROUP_CLOSE_BRACKET) - lastKwEnd)
                }
            }
            lastKwEnd = matcher.end()
        }
        spansBuilder.add(Collections.emptyList(), text.length - lastKwEnd)
        return spansBuilder.create()
    }

    override fun onMouseOverTextStart(event: MouseOverTextEvent) {

    }

    override fun onMouseOverTextEnd(event: MouseOverTextEvent) {

    }
}