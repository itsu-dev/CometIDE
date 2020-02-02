package dev.itsu.cometide

import dev.itsu.cometide.data.EditorData
import dev.itsu.cometide.data.EnvironmentSettings
import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.data.Settings
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.system.SoftwareShutdownEvent
import dev.itsu.cometide.lang.BaseLang
import dev.itsu.cometide.plugin.PluginManager
import dev.itsu.cometide.ui.UIManager
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Stage

class Main : Application() {

    override fun start(primaryStage: Stage) {
        val scene = Scene(UIManager.getInstance().getRoot(), 800.0, 500.0, Color.web("#282C34"))
        UIManager.getInstance().onSceneCreated(primaryStage, scene)
    }

}

fun main(args: Array<String>) {
    setUpProperties()
    setUpShutdownHook()
    setUp()
    Application.launch(Main::class.java, *args)
}

private fun setUpShutdownHook() =  Runtime.getRuntime().addShutdownHook(Thread {
    EventManager.getInstance().callEvent(SoftwareShutdownEvent())
})

private fun setUpProperties() {
   System.setProperty("prism.lcdtext", "false")
}

private fun setUp() {
    EnvironmentSettings.init()
    Settings.getInstance().init()
    BaseLang.init(Settings.getInstance().language, Settings.getInstance().fallbackLanguage)
    EditorData.getInstance().init()
    ProjectData.getInstance().init()
    PluginManager.getInstance().loadPlugins(EnvironmentSettings.PLUGINS_DIRECTORY_PATH)
}