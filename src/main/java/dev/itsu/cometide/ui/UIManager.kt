package dev.itsu.cometide.ui

import dev.itsu.cometide.data.EnvironmentSettings
import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.data.Settings
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.MainStageClosedEvent
import dev.itsu.cometide.event.events.ui.TabPaneTabOpenedEvent
import dev.itsu.cometide.event.events.ui.UICreatedEvent
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.contentbase.ContentBaseImpl
import dev.itsu.cometide.ui.contentbase.menubar.MenuBarImpl
import dev.itsu.cometide.ui.contentbase.tabpane.tab.TabImpl
import dev.itsu.cometide.ui.contentbase.toolbar.ToolBarImpl
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File

class UIManager {

    private val root = VBox()
    val menuBar = MenuBarImpl()
    val toolBar = ToolBarImpl()
    val splitPane = ContentBaseImpl()

    private lateinit var primaryStage: Stage

    companion object {
        private var instance: UIManager? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: UIManager().also { instance = it }
        }
    }

    private lateinit var scene: Scene

    init {
        root.children.addAll(menuBar.getContent(), toolBar.getContent(), splitPane.getContent())
    }

    fun getRoot(): VBox = root

    fun onSceneCreated(stage: Stage, scene: Scene) {
        this.primaryStage = stage
        primaryStage.onCloseRequest = EventHandler { EventManager.getInstance().callEvent(MainStageClosedEvent()) }
        primaryStage.title = "CometIDE"

        this.scene = scene
        primaryStage.scene = this.scene

        setUpCSS()

        menuBar.onSceneCreated(scene)
        toolBar.onSceneCreated(scene)
        splitPane.onSceneCreated(scene)

        EventManager.getInstance().callEvent(UICreatedEvent())

        primaryStage.show()
    }

    private fun setUpCSS() {
        scene.stylesheets?.clear()
        Application.setUserAgentStylesheet("MODENA")
        setCSS(Settings.theme)
    }

    fun setCSS(name: String) {
        val style = File("${EnvironmentSettings.THEMES_DIRECTORY_PATH}${File.separator}$name${File.separator}style.css")
        if (style.exists()) loadCSS(style.toURI().toString())

        val editor = File("${EnvironmentSettings.THEMES_DIRECTORY_PATH}${File.separator}$name${File.separator}editor.css")
        if (editor.exists()) loadCSS(editor.toURI().toString())

        Settings.theme = name
    }

    fun loadCSS(uri: String) {
        val styleSheets = scene.stylesheets
        if (!styleSheets.contains(uri)) {
            styleSheets.add(uri)
        }
    }

    fun openFile(treeItemData: TreeItemData) {
        splitPane.getEditorPane().addTab(TabImpl(treeItemData))
        if (!ProjectData.isOpening(treeItemData.path)) ProjectData.addOpeningFile(treeItemData.path)
        EventManager.getInstance().callEvent(TabPaneTabOpenedEvent(treeItemData))
    }

    fun setTitle(text: String) {
        primaryStage.title = text
    }
}