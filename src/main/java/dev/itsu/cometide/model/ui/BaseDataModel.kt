package dev.itsu.cometide.model.ui

import dev.itsu.cometide.model.TreeItemData
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

class BaseDataModel {

    val selectingProjectTreeItem: ObjectProperty<TreeItemData> = SimpleObjectProperty(null)

    fun setSelectingProjectTreeItem(treeItemData: TreeItemData) = selectingProjectTreeItem.set(treeItemData)
}