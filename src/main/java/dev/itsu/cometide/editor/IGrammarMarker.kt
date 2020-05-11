package dev.itsu.cometide.editor

import dev.itsu.cometide.editor.model.ParseConsequence

interface IGrammarMarker {

    fun getConsequence(): ParseConsequence

}