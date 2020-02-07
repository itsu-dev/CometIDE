package dev.itsu.cometide.ui.part.projecttree

import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.ProjectTreeItemSelectedEvent
import dev.itsu.cometide.model.TreeItemData
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.layout.AnchorPane
import org.apache.commons.io.comparator.ExtensionFileComparator
import java.io.File

class ProjectTree : AnchorPane() {

    private val treeView = TreeView<TreeItemData>()

    init {
        setTopAnchor(treeView, 0.0)
        setBottomAnchor(treeView, 0.0)
        setLeftAnchor(treeView, 0.0)
        setRightAnchor(treeView, 0.0)

        treeView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null)
                EventManager.callEvent(ProjectTreeItemSelectedEvent(newValue.value))
            //presenter.onItemSelect(newValue.value)
        }
        treeView.cellFactory = ProjectTreeCellFactory()
        children.add(treeView)
        /*
        setOnMouseClicked {
            when (it.button) {
                MouseButton.SECONDARY -> {
                    presenter.onRightClick(it, selectionModel.selectedItem)
                    return@setOnMouseClicked
                }
            }

            if (it.clickCount == 2) {
                presenter.onDoubleClick(it, selectionModel.selectedItem)
                EventManager.getInstance().callEvent(ProjectTreeItemDoubleClickedEvent(it, selectionModel.selectedItem))
            }
        }

         */
    }

    fun reload(path: String) {
        val rootFile = File(path)
        if (!rootFile.isDirectory) return

        val rootItem = TreeItem<TreeItemData>(TreeItemData(rootFile.name, rootFile.absolutePath, true, TreeItemData.Type.GROUP))
        rootItem.isExpanded = true

        treeView.root = rootItem

        processDirectory(rootItem, rootFile)
    }

    fun getTreeView(): TreeView<TreeItemData> = treeView

    private fun processDirectory(parent: TreeItem<TreeItemData>, rootFile: File) {
        var node: TreeItem<TreeItemData>
        (arrayFiles(rootFile.listFiles()) ?: return).forEach {
            if (it.isDirectory) {
                node = TreeItem(TreeItemData(it.name, it.absolutePath, false, TreeItemData.Type.GROUP))
                processDirectory(node, it)
            } else {
                node = TreeItem(TreeItemData(it.name, it.absolutePath, false, TreeItemData.Type.ITEM))
            }
            parent.children.add(node)
        }
    }

    private fun arrayFiles(files: Array<File>?): List<File>? {
        files ?: return null
        return files.toMutableList().apply {
            this.sort()
            this.sortWith(ExtensionFileComparator.EXTENSION_INSENSITIVE_COMPARATOR)
            this.sortBy { !it.isDirectory }
        }
    }

}