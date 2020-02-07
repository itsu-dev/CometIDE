package dev.itsu.cometide.model.ui

import dev.itsu.cometide.model.TreeItemData
import javafx.beans.property.*

class BaseDataModel {

    val selectingProjectTreeItem: ObjectProperty<TreeItemData> = SimpleObjectProperty(null)
    val openingProjects: ListProperty<TreeItemData> = SimpleListProperty()
    val projectRoot: StringProperty = SimpleStringProperty()

    fun setSelectingProjectTreeItem(treeItemData: TreeItemData) = selectingProjectTreeItem.set(treeItemData)
    fun addOpeningProject(treeItemData: TreeItemData) = openingProjects.add(treeItemData)
    fun removeOpeningProject(treeItemData: TreeItemData) = openingProjects.remove(treeItemData)
    fun isOpeningProject(treeItemData: TreeItemData): Boolean = openingProjects.contains(treeItemData)
    fun setProjectRoot(path: String) = projectRoot.set(path)
}