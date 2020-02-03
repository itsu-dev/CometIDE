package dev.itsu.cometide.ui.part.pathholder

import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.contentbase.toolbar.part.pathholder.PathHolderCell
import javafx.geometry.NodeOrientation
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import java.io.File

class PathHolder : HBox() {

    init {
        nodeOrientation = NodeOrientation.RIGHT_TO_LEFT
        alignment = Pos.CENTER_RIGHT
    }

    fun reload(treeItemData: TreeItemData) {
        children.clear()

        var file = File(treeItemData.path)
        val projectName = File(ProjectData.project.root).name

        while (true) {
            if (file.name == projectName) {
                createCell(TreeItemData(file.name, file.absolutePath, true, if (file.isDirectory) TreeItemData.Type.GROUP else TreeItemData.Type.ITEM))
                break
            } else {
                createCell(TreeItemData(file.name, file.absolutePath, false, if (file.isDirectory) TreeItemData.Type.GROUP else TreeItemData.Type.ITEM))
                file = file.parentFile
                if (file == null) break
            }
        }
    }

    fun createCell(treeItemData: TreeItemData) {
        children.add(PathHolderCell(treeItemData))
    }

}