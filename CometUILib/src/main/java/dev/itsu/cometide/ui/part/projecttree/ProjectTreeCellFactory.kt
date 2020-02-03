package dev.itsu.cometide.ui.part.projecttree

import dev.itsu.cometide.model.TreeItemData
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeView
import javafx.util.Callback

class ProjectTreeCellFactory  : Callback<TreeView<TreeItemData>, TreeCell<TreeItemData>> {
    override fun call(param: TreeView<TreeItemData>): TreeCell<TreeItemData> = ProjectTreeCell()
}