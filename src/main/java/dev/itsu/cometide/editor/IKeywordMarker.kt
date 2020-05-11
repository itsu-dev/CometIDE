package dev.itsu.cometide.editor

import org.fxmisc.richtext.model.StyleSpans

interface IKeywordMarker {

    fun mark(text: String): StyleSpans<Collection<String>>

}