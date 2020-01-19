package dev.itsu.cometide.ui.contentbase.toolbar.part.pathholder

import dev.itsu.cometide.model.TreeItemData
import javafx.geometry.NodeOrientation
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.HBox

class PathHolderImpl : IPathHolder.UI {

    private val presenter = PathHolderPresenter(this)
    val hBox = HBox()

    init {
        hBox.nodeOrientation = NodeOrientation.RIGHT_TO_LEFT
    }

    override fun reload(treeItemData: TreeItemData) {
        presenter.reload(treeItemData)
    }

    override fun getContent(): Node {
        return hBox
    }

    override fun onSceneCreated(scene: Scene) {

    }
}