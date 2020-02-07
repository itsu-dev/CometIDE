package dev.itsu.cometide.ui.part.tab

import dev.itsu.cometide.dao.ProjectDao
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.util.IconCreator
import javafx.scene.control.Tab

open class TabContent(val treeItemData: TreeItemData) : Tab() {
    init {
        this.text = treeItemData.name
        this.graphic = IconCreator.createImageFromData(treeItemData)
        this.isClosable = true

        setOnClosed {
            ProjectDao.project.openingFiles.remove(treeItemData.path)
        }
    }
}