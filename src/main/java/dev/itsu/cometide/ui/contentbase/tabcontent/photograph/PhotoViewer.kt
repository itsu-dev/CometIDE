package dev.itsu.cometide.ui.contentbase.tabcontent.photograph

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.contentbase.tabcontent.ITabContent
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import org.fxmisc.flowless.VirtualizedScrollPane
import java.io.File

class PhotoViewer(treeItemData: TreeItemData) : ITabContent {

    private val rootPane = BorderPane()
    private val imageView = ImageView()

    init {
        imageView.image = Image(File(treeItemData.path).inputStream())
        rootPane.center = imageView
    }

    override fun getContent(): Node = rootPane

}