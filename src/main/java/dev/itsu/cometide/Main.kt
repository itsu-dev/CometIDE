package dev.itsu.cometide

import dev.itsu.cometide.data.EnvironmentSettings
import dev.itsu.cometide.data.Settings
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.system.SoftwareShutdownEvent
import dev.itsu.cometide.event.events.ui.MainStageClosedEvent
import dev.itsu.cometide.lang.BaseLang
import dev.itsu.cometide.plugin.PluginManager
import dev.itsu.cometide.ui.UIManager
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.stage.Stage

class Main : Application() {

    override fun start(primaryStage: Stage) {
        UIManager.launch(primaryStage)
    }

}

fun main(args: Array<String>) {
    /*
    setUpProperties()
    setUpShutdownHook()
    setUp()

     */
    Application.launch(Main::class.java, *args)
}

private fun setUpShutdownHook() =  Runtime.getRuntime().addShutdownHook(Thread {
    EventManager.callEvent(SoftwareShutdownEvent())
})

private fun setUpProperties() {
   System.setProperty("prism.lcdtext", "false")
}

private fun setUp() {
    BaseLang.init(Settings.language, Settings.fallbackLanguage)
    PluginManager.loadPlugins(EnvironmentSettings.PLUGINS_DIRECTORY_PATH)
}