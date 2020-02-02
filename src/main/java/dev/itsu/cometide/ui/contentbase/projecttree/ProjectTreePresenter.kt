package dev.itsu.cometide.ui.contentbase.projecttree

import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.ProjectTreeItemDoubleClickedEvent
import dev.itsu.cometide.event.events.ui.ProjectTreeItemLeftClickedEvent
import dev.itsu.cometide.event.events.ui.ProjectTreeItemRightClickedEvent
import dev.itsu.cometide.event.events.ui.ProjectTreeItemSelectedEvent
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.UIManager
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.input.MouseEvent
import org.apache.commons.io.comparator.ExtensionFileComparator
import java.io.File

class ProjectTreePresenter(val projectTreeImpl: ProjectTreeImpl) : IProjectTree.Presenter {

    override fun reload(path: String) {
        val rootFile = File(path)
        if (!rootFile.isDirectory) return

        val rootItem = TreeItem<TreeItemData>(TreeItemData(rootFile.name, rootFile.absolutePath, true, TreeItemData.Type.GROUP))
        rootItem.isExpanded = true

        processDirectory(rootItem, rootFile)
        (projectTreeImpl.getContent() as TreeView<TreeItemData>).root = rootItem
    }

    override fun processDirectory(parent: TreeItem<TreeItemData>, rootFile: File) {
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

    override fun arrayFiles(files: Array<File>?): List<File>? {
        files ?: return null
        return files.toMutableList().apply {
            this.sort()
            this.sortWith(ExtensionFileComparator.EXTENSION_INSENSITIVE_COMPARATOR)
            this.sortBy { !it.isDirectory }
        }
    }

    override fun onItemSelect(newValue: TreeItemData) {
        val treeItemData = newValue
        ProjectData.getInstance().project.selectingFile = treeItemData.path
        UIManager.getInstance().toolBar.reload(newValue)
        EventManager.getInstance().callEvent(ProjectTreeItemSelectedEvent(newValue))
    }

    override fun onDoubleClick(mouseEvent: MouseEvent, selectedItem: TreeItem<TreeItemData>) {
        if (selectedItem.value.type != TreeItemData.Type.GROUP) {
            if (!ProjectData.getInstance().isOpening(selectedItem.value.path)) {
                UIManager.getInstance().openFile(selectedItem.value)
            } else {
                UIManager.getInstance().splitPane.getEditorPane().setTab(selectedItem.value.name)
            }
        }
        EventManager.getInstance().callEvent(ProjectTreeItemDoubleClickedEvent(mouseEvent, selectedItem))
    }

    override fun onRightClick(mouseEvent: MouseEvent, selectedItem: TreeItem<TreeItemData>) {
        EventManager.getInstance().callEvent((ProjectTreeItemRightClickedEvent(mouseEvent, selectedItem)))
    }

}