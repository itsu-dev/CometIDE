package dev.itsu.cometide.model

data class Project(
        var root: String,
        var name: String,
        var openingFiles: MutableList<String>,
        var selectingFile: String,
        var openingTab: Int
) {
    var isEmptyProject = false

    companion object {
        const val NOT_SELECTED = -1

        fun emptyProject(): Project = Project("", "", mutableListOf(), "", NOT_SELECTED).also {
            it.isEmptyProject = true
        }
    }
}