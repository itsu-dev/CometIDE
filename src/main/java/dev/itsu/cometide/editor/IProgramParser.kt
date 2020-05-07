package dev.itsu.cometide.editor

import dev.itsu.cometide.editor.model.ParseConsequence

interface IProgramParser {

    fun parse(text: String): ParseConsequence

}