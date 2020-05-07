package dev.itsu.cometide.ui.editor.image

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.part.tab.TabContent
import javafx.scene.Group
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import java.io.FileInputStream

class ImageViewerImpl(treeItemData: TreeItemData) : TabContent(treeItemData) {

    private val imageView = ImageView()

    init {
        val image = Image(FileInputStream(treeItemData.path))
        val stack = StackPane()
        val scrollView = ScrollPane(stack)

        scrollView.prefWidth = Double.MAX_VALUE
        scrollView.prefHeight = Double.MAX_VALUE

        imageView.isPreserveRatio = true
        imageView.image = image

        stack.children.add(imageView)
        stack.translateXProperty()
                .bind(scrollView.widthProperty().subtract(stack.widthProperty())
                        .divide(2))
        stack.translateYProperty()
                .bind(scrollView.heightProperty().subtract(stack.heightProperty())
                        .divide(2))

        this.content = scrollView


        /*
        val scrollView = ScrollPane(imageView)
        scrollView.prefWidth = Double.MAX_VALUE
        scrollView.prefHeight = Double.MAX_VALUE

        this.content = scrollView

        val image = Image(FileInputStream(treeItemData.path))
        scrollView.width
        scrollView.height
        imageView.isPreserveRatio = true
        imageView.image = image
        imageView.fitWidthProperty().bind(scrollView.widthProperty())
        imageView.fitHeightProperty().bind(scrollView.heightProperty())

         */
    }

}