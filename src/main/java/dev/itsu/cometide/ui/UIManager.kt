package dev.itsu.cometide.ui

import com.jfoenix.controls.JFXDecorator
import com.jfoenix.svg.SVGGlyph
import com.jfoenix.svg.SVGGlyphLoader
import dev.itsu.cometide.dao.SettingsDao
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.MainStageClosedEvent
import dev.itsu.cometide.event.events.ui.UICreatedEvent
import dev.itsu.cometide.model.ui.BaseDataModel
import dev.itsu.cometide.model.ui.BottomBarDataModel
import dev.itsu.cometide.ui.controller.BaseController
import dev.itsu.cometide.ui.controller.BottomBarController
import dev.itsu.cometide.ui.util.IconCreator
import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.io.File
import java.lang.IllegalStateException

object UIManager {

    private lateinit var primaryStage: Stage
    private lateinit var scene: Scene
    private var initialized = false

    private lateinit var baseController: BaseController
    private lateinit var bottomBarController: BottomBarController

    fun launch(stage: Stage) {
        if (initialized) throw IllegalStateException("UIManager has already initialized!")
        initialized = true
        primaryStage = stage

        val baseFXMLLoader = FXMLLoader(javaClass.classLoader.getResource("fxml/base.fxml"))
        val baseNode = baseFXMLLoader.load<AnchorPane>()
        baseController = baseFXMLLoader.getController()
        baseController.initializeData(BaseDataModel())

        val decorator = JFXDecorator(primaryStage, baseNode, false, true, true)
        decorator.isCustomMaximize = true
        decorator.graphic = IconCreator.createImageView(IconCreator.createImage("img/comet20.png"))

        primaryStage.onCloseRequest = EventHandler { EventManager.callEvent(MainStageClosedEvent()) }
        primaryStage.title = "CometIDE"
        primaryStage.scene = Scene(decorator, 800.0, 500.0, Color.web("#282C34")).also { scene = it }

        setUpCSS()
        EventManager.callEvent(UICreatedEvent())

        primaryStage.show()
    }

    fun getBaseController(): BaseController = baseController
    fun getBottomBarController(): BottomBarController = bottomBarController

    private fun setUpCSS() {
        scene.stylesheets?.clear()
        Application.setUserAgentStylesheet("MODENA")
        setCSS(SettingsDao.THEME)
    }

    fun setCSS(name: String) {
        val style = File("${SettingsDao.ENVIRONMENT_VARIABLES["THEMES_DIRECTORY_PATH"]}${File.separator}$name${File.separator}style.css")
        if (style.exists()) loadCSS(style.toURI().toString())

        val editor = File("${SettingsDao.ENVIRONMENT_VARIABLES["THEMES_DIRECTORY_PATH"]}${File.separator}$name${File.separator}editor.css")
        if (editor.exists()) loadCSS(editor.toURI().toString())

        SettingsDao.THEME = name
    }

    fun loadCSS(uri: String) {
        val styleSheets = scene.stylesheets
        if (!styleSheets.contains(uri)) {
            styleSheets.add(uri)
        }
    }

    fun setTitle(text: String) {
        primaryStage.title = text
    }

    fun setBottomBarController(bottomBarController: BottomBarController) {
        this.bottomBarController = bottomBarController
    }
}