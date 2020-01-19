package dev.itsu.cometide.model

data class TreeItemData (
        val name: String,
        val path: String,
        val root: Boolean,
        val type: Type
) {
    enum class Type {
        ITEM,
        GROUP
    }
}
