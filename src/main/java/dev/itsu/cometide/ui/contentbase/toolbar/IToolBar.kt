package dev.itsu.cometide.ui.contentbase.toolbar

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.UIInterface
import dev.itsu.cometide.ui.contentbase.toolbar.part.pathholder.PathHolderImpl

interface IToolBar {

    interface UI : UIInterface.UI {
        fun reload(treeItemData: TreeItemData)
        fun getPathHolder(): PathHolderImpl
        fun createActionBar()
    }

    interface Presenter : UIInterface.Presenter {
        fun reload(treeItemData: TreeItemData)
        fun onScreenShotButtonClicked()
        fun onFindButtonClicked()
        fun onProjectSettingButtonClicked()
    }

}