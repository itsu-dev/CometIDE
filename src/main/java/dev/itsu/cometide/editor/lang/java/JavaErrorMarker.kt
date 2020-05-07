package dev.itsu.cometide.editor.lang.java

import dev.itsu.cometide.editor.IMarker
import dev.itsu.cometide.editor.model.ParseArea
import dev.itsu.cometide.editor.model.ParseConsequence
import dev.itsu.cometide.editor.model.ParseProblem

class JavaErrorMarker : IMarker {

    private val mConsequence = ParseConsequence()

    fun parse(problems: List<ParseProblem>) {
        problems.forEach {
            mConsequence.addParseArea(ParseArea(it.paragraph, it.from, it.to, it.style))
            mConsequence.addProblem(it)
        }
    }

    override fun getConsequence(): ParseConsequence = mConsequence

}
