package dev.itsu.cometide.ui.contentbase.toolbar

import dev.itsu.cometide.model.TreeItemData

class ToolBarPresenter(val toolBarImpl: ToolBarImpl) : IToolBar.Presenter {

    override fun reload(treeItemData: TreeItemData) = toolBarImpl.getPathHolder().reload(treeItemData)

}