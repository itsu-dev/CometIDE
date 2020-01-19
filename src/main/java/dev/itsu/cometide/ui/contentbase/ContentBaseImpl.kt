package dev.itsu.cometide.ui.contentbase

import dev.itsu.cometide.data.RuntimeData
import dev.itsu.cometide.ui.contentbase.projecttree.ProjectTreeImpl
import dev.itsu.cometide.ui.contentbase.tabpane.TabPaneImpl
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.scene.layout.StackPane

class ContentBaseImpl : IContentBase.UI {

    private val horizontal = SplitPane()
    private val vertical = SplitPane()
    private lateinit var left: StackPane
    private lateinit var right: StackPane
    private val bottom = StackPane()

    private val projectTree = ProjectTreeImpl()
    private lateinit var editor: TabPaneImpl

    init {
        vertical.setDividerPositions(0.2, 0.8)
        vertical.orientation = Orientation.VERTICAL

        horizontal.setDividerPositions(0.2, 0.8)
        horizontal.orientation = Orientation.HORIZONTAL

        createLeftPane()
        createRightPane()
        createBottomPane()

        horizontal.items.addAll(left, right)
        vertical.items.addAll(horizontal, bottom)
    }

    override fun createLeftPane() {
        if (RuntimeData.getInstance().projectRoot != null) projectTree.reload(RuntimeData.getInstance().projectRoot!!)
        left = StackPane(projectTree.getContent())
    }

    override fun createRightPane() {
        editor = TabPaneImpl()
        right = StackPane(editor.getContent())
    }

    override fun createBottomPane() {

    }

    override fun onSceneCreated(scene: Scene) {
        vertical.prefWidthProperty().bind(scene.widthProperty())
        vertical.prefHeightProperty().bind(scene.heightProperty())

        projectTree.onSceneCreated(scene)
        editor.onSceneCreated(scene)
    }

    override fun getContent(): Node = vertical

    override fun getLeftPane(): StackPane = left

    override fun getRightPane(): StackPane = right

    override fun getBottomPane(): StackPane = bottom

    override fun getEditorPane(): TabPaneImpl  = editor

}