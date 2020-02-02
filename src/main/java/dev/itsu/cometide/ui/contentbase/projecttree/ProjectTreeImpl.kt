package dev.itsu.cometide.ui.contentbase.projecttree

import dev.itsu.cometide.model.TreeItemData
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.TreeView
import javafx.scene.input.MouseButton

class ProjectTreeImpl : IProjectTree.UI {

    private val presenter = ProjectTreePresenter(this)

    private val treeView = TreeView<TreeItemData>()

    init {
        treeView.selectionModel.selectedItemProperty().addListener { _, _, newValue -> if (newValue != null) presenter.onItemSelect(newValue.value) }
        treeView.cellFactory = ProjectTreeCellFactory()
        treeView.setOnMouseClicked {
            when (it.button) {
                MouseButton.SECONDARY -> {
                    presenter.onRightClick(it, treeView.selectionModel.selectedItem)
                    return@setOnMouseClicked
                }
            }

            if (it.clickCount == 2) {
                presenter.onDoubleClick(it, treeView.selectionModel.selectedItem)
            }
        }
    }

    override fun reload(path: String) = presenter.reload(path)

    override fun getContent(): Node = treeView

    override fun onSceneCreated(scene: Scene) {

    }

}