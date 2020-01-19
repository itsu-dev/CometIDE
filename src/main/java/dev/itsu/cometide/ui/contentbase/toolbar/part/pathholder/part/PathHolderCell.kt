package dev.itsu.cometide.ui.contentbase.toolbar.part.pathholder.part

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.util.IconCreator
import javafx.geometry.Insets
import javafx.geometry.NodeOrientation
import javafx.scene.control.Label
import javafx.scene.layout.HBox

class PathHolderCell(treeItemData: TreeItemData) : HBox() {

    private val label = Label(treeItemData.name)

    init {
        label.padding = Insets(0.0, 4.0, 0.0, 4.0)
        padding = Insets(4.0, 4.0, 4.0, 4.0)
        nodeOrientation = NodeOrientation.LEFT_TO_RIGHT
        children.addAll(IconCreator.createImageFromData(treeItemData), label, IconCreator.createImageView(IconCreator.ICON_NEXT))
    }

}