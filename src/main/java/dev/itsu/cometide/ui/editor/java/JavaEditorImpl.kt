package dev.itsu.cometide.ui.editor.java

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.editor.AbstractEditor
import dev.itsu.cometide.ui.editor.java.visitor.ErrorMarker
import javafx.scene.control.Label
import javafx.stage.Popup
import org.fxmisc.richtext.StyleClassedTextArea
import org.fxmisc.richtext.event.MouseOverTextEvent
import org.fxmisc.richtext.model.StyleSpan
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import org.fxmisc.richtext.model.TwoDimensional
import java.util.regex.Pattern

class JavaEditorImpl(treeItemData: TreeItemData) : AbstractEditor(treeItemData, "java") {

    val KEYWORDS = arrayOf(
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    )

    private val KEYWORD_PATTERN = "\\b(" + java.lang.String.join("|", *KEYWORDS) + ")\\b"
    private val PAREN_PATTERN = "\\(|\\)"
    private val BRACE_PATTERN = "\\{|\\}"
    private val BRACKET_PATTERN = "\\[|\\]"
    private val SEMICOLON_PATTERN = "\\;"
    private val STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\""
    private val COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"

    private val PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    )

    val highlightQueue = mutableListOf<StyleSpan<Collection<String>>>()
    private val popup = Popup()
    private val label = Label()

    init {
        popup.content.addAll(label)
        popup.width = 200.0
        label.style = "-fx-background-color: #37474F"
        label.isWrapText = true
    }

    override fun setUp(codeArea: StyleClassedTextArea) {

    }

    override fun onAppliedHighlighting() {
        CometJavaParser.parse(codeArea.text, this)
    }

    override fun computeHighlighting(text: String): StyleSpans<Collection<String>> {
        val matcher = PATTERN.matcher(text)
        var lastKwEnd = 0
        val spansBuilder = StyleSpansBuilder<Collection<String>>()
        spansBuilder.addAll(highlightQueue)
        while (matcher.find()) {
            val styleClass = (if (matcher.group("KEYWORD") != null) "keyword" else if (matcher.group("PAREN") != null) "paren" else if (matcher.group("BRACE") != null) "brace" else if (matcher.group("BRACKET") != null) "bracket" else if (matcher.group("SEMICOLON") != null) "semicolon" else if (matcher.group("STRING") != null) "string" else if (matcher.group("COMMENT") != null) "comment" else null)!!
            spansBuilder.add(emptyList(), matcher.start() - lastKwEnd)
            spansBuilder.add(setOf(styleClass), matcher.end() - matcher.start())
            lastKwEnd = matcher.end()
        }
        spansBuilder.add(emptyList(), text.length - lastKwEnd)
        return spansBuilder.create()
    }

    override fun onMouseOverTextStart(event: MouseOverTextEvent) {
        ErrorMarker.problems.stream()
                .filter {
                    if (!it.location.isPresent) return@filter false
                    val position = it.location.get().toRange().get().begin
                    val mousePosition = codeArea.offsetToPosition(event.characterIndex, TwoDimensional.Bias.Forward)
                    return@filter position.line - 1 == mousePosition.major && position.column - 1 == mousePosition.minor
                }
                .forEach {
                    label.text = it.message
                    popup.show(codeArea, event.screenPosition.x, event.screenPosition.y + 10)
                }
    }

    override fun onMouseOverTextEnd(event: MouseOverTextEvent) {
        popup.hide()
    }
}