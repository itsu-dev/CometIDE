package dev.itsu.cometide.editor

import dev.itsu.cometide.editor.model.ParseConsequence

interface IMarker {

    fun getConsequence(): ParseConsequence

}