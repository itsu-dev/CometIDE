package dev.itsu.cometide.ui.contentbase.toolbar.part.pathholder

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.UIInterface

interface IPathHolder {
    interface UI : UIInterface.UI{
        fun reload(treeItemData: TreeItemData)
    }

    interface Presenter {
        fun reload(treeItemData: TreeItemData)
        fun createCell(treeItemData: TreeItemData)
    }
}