package dev.itsu.cometide.ui.contentbase

import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.ui.contentbase.bottombar.BottomBarImpl
import dev.itsu.cometide.ui.contentbase.projecttree.ProjectTreeImpl
import dev.itsu.cometide.ui.contentbase.tabpane.TabPaneImpl
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.scene.control.Tab
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane

class ContentBaseImpl : IContentBase.UI {

    private val horizontal = SplitPane()
    private val vertical = SplitPane()
    private lateinit var left: StackPane
    private lateinit var right: StackPane
    private lateinit var bottom: BorderPane

    private val projectTree = ProjectTreeImpl()
    private lateinit var editor: TabPaneImpl
    private lateinit var actionTab: TabPaneImpl
    private lateinit var bottomBar: BottomBarImpl

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
        if (!ProjectData.project.isEmptyProject) projectTree.reload(ProjectData.project.root)
        left = StackPane(projectTree.getContent())
    }

    override fun createRightPane() {
        editor = TabPaneImpl()
        right = StackPane(editor.getContent())
    }

    override fun createBottomPane() {
        actionTab = TabPaneImpl()
        bottomBar = BottomBarImpl()

        bottom = BorderPane()
        bottom.center = actionTab.getContent()
        bottom.bottom = bottomBar.getContent()

        actionTab.addTab(Tab("Run"))
    }

    override fun onSceneCreated(scene: Scene) {
        vertical.prefWidthProperty().bind(scene.widthProperty())
        vertical.prefHeightProperty().bind(scene.heightProperty())

        projectTree.onSceneCreated(scene)
        editor.onSceneCreated(scene)
    }

    override fun getContent(): Node = vertical

    override fun getEditorPane(): TabPaneImpl  = editor

    override fun getBottomBar(): BottomBarImpl = bottomBar

    override fun getProjectTree(): ProjectTreeImpl = projectTree

}