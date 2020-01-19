package dev.itsu.cometide.ui.contentbase.projecttree

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.UIInterface
import javafx.scene.control.TreeItem
import javafx.scene.input.MouseEvent
import java.io.File

interface IProjectTree {

    interface UI : UIInterface.UI {
        fun reload(path: String)
    }

    interface Presenter : UIInterface.Presenter {
        fun reload(path: String)
        fun processDirectory(parent: TreeItem<TreeItemData>, rootFile: File)
        fun arrayFiles(files: Array<File>?): List<File>?
        fun onItemSelect(newValue: TreeItemData)
        fun onDoubleClick(mouseEvent: MouseEvent, selectedItem: TreeItem<TreeItemData>)
        fun onRightClick(mouseEvent: MouseEvent, selectedItem: TreeItem<TreeItemData>)
    }

}