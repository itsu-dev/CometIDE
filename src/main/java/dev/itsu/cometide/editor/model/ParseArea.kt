package dev.itsu.cometide.editor.model

data class ParseArea(
        val paragraph: Int,
        val from: Int,
        val to: Int,
        val style: Set<String>
)