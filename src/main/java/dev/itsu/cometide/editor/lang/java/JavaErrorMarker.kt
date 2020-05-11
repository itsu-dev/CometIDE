package dev.itsu.cometide.editor.lang.java

import dev.itsu.cometide.editor.IGrammarMarker
import dev.itsu.cometide.editor.model.ParseArea
import dev.itsu.cometide.editor.model.ParseConsequence
import dev.itsu.cometide.editor.model.ParseProblem

class JavaErrorMarker : IGrammarMarker {

    private val mConsequence = ParseConsequence()

    fun parse(problems: List<ParseProblem>) {
        problems.forEach {
            mConsequence.parseAreas.add(ParseArea(it.paragraph, it.from, it.to, it.style))
            mConsequence.problems.add(it)
        }
    }

    override fun getConsequence(): ParseConsequence = mConsequence

}
