package dev.itsu.cometide.ui.editor.common

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.part.tab.TabContent

class CommonCodeEditor(treeItemData: TreeItemData) : TabContent(treeItemData) {

    private val editor = EditorComponent(treeItemData)

    init {
        content = editor
    }

}