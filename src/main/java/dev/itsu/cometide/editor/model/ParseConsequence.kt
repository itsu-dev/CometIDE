package dev.itsu.cometide.editor.model

class ParseConsequence {

    private val parseAreas = mutableListOf<ParseArea>()
    private var problems = mutableListOf<ParseProblem>()
    private var messages = mutableListOf<String>()

    fun addParseArea(parseArea: ParseArea) = parseAreas.add(parseArea)
    fun getParseAreas() = parseAreas

    fun addMessage(message: String) = messages.add(message)
    fun getMessages() = messages

    fun addProblem(problem: ParseProblem) = problems.add(problem)
    fun getProblems() = problems

}