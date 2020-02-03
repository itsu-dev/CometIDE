package dev.itsu.cometide.ui.contentbase.toolbar

import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.MenuItemClickedEvent
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.contentbase.toolbar.part.actionbar.ActionBarButton
import javafx.scene.Scene
import javafx.scene.Node
import dev.itsu.cometide.ui.contentbase.toolbar.part.pathholder.PathHolderImpl
import dev.itsu.cometide.ui.util.IconCreator
import javafx.geometry.NodeOrientation
import javafx.geometry.Pos
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority

class ToolBarImpl : IToolBar.UI {

    private val presenter = ToolBarPresenter(this)

    private val toolBar = GridPane()
    private val pathHolder = PathHolderImpl()
    private val actionBar = HBox()

    init {
        toolBar.styleClass.add("toolbar")
        toolBar.alignment = Pos.CENTER
        toolBar.prefHeight = 36.0

        createActionBar()

        val col1 = ColumnConstraints()
        col1.percentWidth = 60.0
        col1.hgrow = Priority.ALWAYS

        val col2 = ColumnConstraints()
        col2.percentWidth = 40.0
        col2.hgrow = Priority.ALWAYS

        toolBar.columnConstraints.addAll(col1, col2)
        toolBar.addColumn(0, pathHolder.getContent())
        toolBar.addColumn(1, actionBar)
    }

    override fun createActionBar() {
        actionBar.alignment = Pos.CENTER_RIGHT
        actionBar.spacing = 4.0

        val find = ActionBarButton(IconCreator.createImage("img/icon/icon_find.png")).also {
            it.setOnMouseClicked { EventManager.callEvent(MenuItemClickedEvent("menubar.edit.find")) }
        }

        val projectSetting = ActionBarButton(IconCreator.createImage("img/icon/icon_project_setting.png")).also {
            it.setOnMouseClicked { EventManager.callEvent(MenuItemClickedEvent("menubar.file.project_setting")) }
        }

        val screenshot = ActionBarButton(IconCreator.createImage("img/icon/icon_screenshot.png")).also {
            it.setOnMouseClicked { EventManager.callEvent(MenuItemClickedEvent("actionbar.screenshot")) }
        }

        actionBar.children.addAll(find, projectSetting, screenshot)
    }

    override fun reload(treeItemData: TreeItemData) = presenter.reload(treeItemData)

    override fun getPathHolder(): PathHolderImpl = pathHolder

    override fun getContent(): Node = toolBar

    override fun onSceneCreated(scene: Scene) {

    }
}