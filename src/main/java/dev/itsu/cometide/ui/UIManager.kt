package dev.itsu.cometide.ui

import dev.itsu.cometide.data.EnvironmentSettings
import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.data.Settings
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.MainStageClosedEvent
import dev.itsu.cometide.event.events.ui.TabPaneTabOpenedEvent
import dev.itsu.cometide.event.events.ui.UICreatedEvent
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.model.ui.BaseDataModel
import dev.itsu.cometide.model.ui.BottomBarDataModel
import dev.itsu.cometide.ui.contentbase.ContentBaseImpl
import dev.itsu.cometide.ui.contentbase.menubar.MenuBarImpl
import dev.itsu.cometide.ui.contentbase.tabpane.tab.TabImpl
import dev.itsu.cometide.ui.contentbase.toolbar.ToolBarImpl
import dev.itsu.cometide.ui.controller.BaseController
import dev.itsu.cometide.ui.controller.BottomBarController
import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.io.File
import java.lang.IllegalStateException

object UIManager {

    private val root = VBox()
    val menuBar = MenuBarImpl()
    val toolBar = ToolBarImpl()
    val splitPane = ContentBaseImpl()

    private lateinit var primaryStage: Stage
    private var initialized = false

    private lateinit var baseController: BaseController
    private lateinit var bottomBarController: BottomBarController

    fun launch(stage: Stage) {
        if (initialized) throw IllegalStateException("UIManager has already initialized!")
        initialized = true

        val baseFXMLLoader = FXMLLoader(javaClass.classLoader.getResource("fxml/base.fxml"))
        baseController = baseFXMLLoader.getController()
        baseController.initializeData(BaseDataModel())

        val bottomBarFXMLLoader = FXMLLoader(javaClass.classLoader.getResource("fxml/bottom_bar.fxml"))
        bottomBarController = bottomBarFXMLLoader.getController()
        bottomBarController.initializeData(BottomBarDataModel())

        primaryStage = stage
        primaryStage.onCloseRequest = EventHandler { EventManager.callEvent(MainStageClosedEvent()) }
        primaryStage.title = "CometIDE"
        primaryStage.scene = Scene(baseFXMLLoader.load() as AnchorPane, 800.0, 500.0, Color.web("#282C34"))

        setUpCSS()
        EventManager.callEvent(UICreatedEvent())

        primaryStage.show()
    }

    fun getBaseController(): BaseController = baseController
    fun getBottomBarController(): BottomBarController = bottomBarController

    /////

    private lateinit var scene: Scene

    init {
        root.children.addAll(menuBar.getContent(), toolBar.getContent(), splitPane.getContent())
    }

    fun getRoot(): VBox = root

    fun onSceneCreated(stage: Stage, scene: Scene) {
        this.primaryStage = stage
        primaryStage.onCloseRequest = EventHandler { EventManager.callEvent(MainStageClosedEvent()) }
        primaryStage.title = "CometIDE"

        this.scene = scene
        primaryStage.scene = this.scene

        setUpCSS()

        menuBar.onSceneCreated(scene)
        toolBar.onSceneCreated(scene)
        splitPane.onSceneCreated(scene)

        EventManager.callEvent(UICreatedEvent())

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
        EventManager.callEvent(TabPaneTabOpenedEvent(treeItemData))
    }

    fun setTitle(text: String) {
        primaryStage.title = text
    }
}