package dev.itsu.cometide.ui.part.projecttree

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.util.IconCreator
import javafx.scene.control.TreeCell

class ProjectTreeCell : TreeCell<TreeItemData>() {

    override fun updateItem(item: TreeItemData?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty || item == null) {
            text = null
            graphic = null
        } else {
            text = item.name
            graphic = IconCreator.createImageFromData(item)
        }
    }

}