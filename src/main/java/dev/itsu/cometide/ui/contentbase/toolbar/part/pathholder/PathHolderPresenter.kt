package dev.itsu.cometide.ui.contentbase.toolbar.part.pathholder

import dev.itsu.cometide.data.RuntimeData
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.contentbase.toolbar.part.pathholder.part.PathHolderCell
import java.io.File

class PathHolderPresenter(val pathHolderImpl: PathHolderImpl) : IPathHolder.Presenter {

    override fun reload(treeItemData: TreeItemData) {
        pathHolderImpl.hBox.children.clear()

        var file = File(treeItemData.path)
        val projectName = File(RuntimeData.getInstance().projectRoot ?: "").name

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

    override fun createCell(treeItemData: TreeItemData) {
        pathHolderImpl.hBox.children.add(PathHolderCell(treeItemData))
    }
}