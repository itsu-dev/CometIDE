package dev.itsu.cometide.editor.lang.java

import com.github.javaparser.JavaParser
import com.github.javaparser.ParserConfiguration
import dev.itsu.cometide.editor.IProgramParser
import dev.itsu.cometide.editor.model.ParseConsequence
import dev.itsu.cometide.editor.model.ParseProblem

object JavaProgramParser : IProgramParser {

    override fun parse(text: String): ParseConsequence {
        val unit = JavaParser().parse(text)
        if (unit.result.isPresent) {
            if (unit.problems.size == 0) {
                val result = unit.result.get()
                val grammarMarker = JavaGrammarMarker()
                result.accept(grammarMarker, "")
                return grammarMarker.getConsequence()

            } else {
                val problems = mutableListOf<ParseProblem>()
                unit.problems.forEach {
                    if (!it.location.isPresent) return@forEach
                    val range = it.location.get().toRange()
                    if (range.isPresent) {
                        problems.add(ParseProblem(range.get().begin.line - 1, range.get().begin.column - 1, range.get().begin.column, it.message, setOf("problem")))
                    }
                }
                return JavaErrorMarker().let {
                    it.parse(problems)
                    it.getConsequence()
                }
            }
        }

        return ParseConsequence()
    }

}