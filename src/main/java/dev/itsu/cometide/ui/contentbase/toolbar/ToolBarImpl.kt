package dev.itsu.cometide.ui.contentbase.toolbar

import dev.itsu.cometide.model.TreeItemData
import javafx.scene.Scene
import javafx.scene.control.ToolBar
import javafx.scene.Node
import dev.itsu.cometide.ui.contentbase.toolbar.part.pathholder.PathHolderImpl

class ToolBarImpl : IToolBar.UI {

    private val presenter = ToolBarPresenter(this)

    private val toolBar = ToolBar()
    private val pathHolder = PathHolderImpl()

    init {
        toolBar.items.add(pathHolder.getContent())
    }

    override fun reload(treeItemData: TreeItemData) = presenter.reload(treeItemData)

    override fun getPathHolder(): PathHolderImpl = pathHolder

    override fun getContent(): Node = toolBar

    override fun onSceneCreated(scene: Scene) {

    }
}