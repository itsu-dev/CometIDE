package dev.itsu.cometide.editor.model

data class ParseProblem(
        val paragraph: Int,
        val from: Int,
        val to: Int,
        val message: String?,
        val style: Set<String>
)