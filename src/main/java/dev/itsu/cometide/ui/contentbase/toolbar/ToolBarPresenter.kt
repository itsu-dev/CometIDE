package dev.itsu.cometide.ui.contentbase.toolbar

import dev.itsu.cometide.model.TreeItemData

class ToolBarPresenter(val toolBarImpl: ToolBarImpl) : IToolBar.Presenter {

    override fun reload(treeItemData: TreeItemData) = toolBarImpl.getPathHolder().reload(treeItemData)

    override fun onScreenShotButtonClicked() {

    }

    override fun onFindButtonClicked() {

    }

    override fun onProjectSettingButtonClicked() {

    }
}