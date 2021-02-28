package dev.itsu.cometide.editor.lang.json

import dev.itsu.cometide.editor.IKeywordMarker
import org.fxmisc.richtext.model.StyleSpan
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import java.util.regex.Pattern

object JsonKeywordMarker : IKeywordMarker {

    private val NUMBER_PATTERN = "-? (?= [1-9]|0(?!\\d) ) \\d+ (\\.\\d+)? ([eE] [+-]? \\d+)? "
    private val BOOLEAN_PATTERN = "true | false | null"
    private val STRING_PATTERN = "\"  ([^\"\\\\]* | \\\\ [\"\\\\bfnrt\\/] | \\\\ u [0-9a-f]{4} )* \""
    private val ARRAY_PATTERN = " \\[ (?: (?&json) (?: , (?&json) )* )? \\s* \\] " // (?&${ARRAY_PATTERN}) |
    private val PAIR_PATTERN = "\\s* (?${STRING_PATTERN}) \\s* : (?&json) "
    private val OBJECT_PATTERN = "\\{ (?: (?&${PAIR_PATTERN}) (?: , (?&${PAIR_PATTERN}) )* )? \\s* \\} "
    private val JSON_PATTERN = "\\s* (? (?:${NUMBER_PATTERN}) | (?$${BOOLEAN_PATTERN}) | (?&${BOOLEAN_PATTERN}) | (?&${STRING_PATTERN}) | (?&${OBJECT_PATTERN}) ) \\s*"

    private val PATTERN = Pattern.compile(
            "(?<NUMBER>" + NUMBER_PATTERN + ")"
                    + "|(?<BOOLEAN>" + BOOLEAN_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    //+ "|(?<ARRAY>" + ARRAY_PATTERN + ")"
                    + "|(?<PAIR>" + PAIR_PATTERN + ")"
                    + "|(?<OBJECT>" + OBJECT_PATTERN + ")"
                    + "|(?<JSON>" + JSON_PATTERN + ")"
    )

    private val highlightQueue = mutableListOf<StyleSpan<Collection<String>>>()

    override fun mark(text: String): StyleSpans<Collection<String>> {
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

}