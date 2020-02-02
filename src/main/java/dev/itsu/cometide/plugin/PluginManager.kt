package dev.itsu.cometide.plugin

import dev.itsu.cometide.plugin.loader.PluginLoader
import java.io.File

object PluginManager {

    lateinit var plugins: MutableMap<String, AbstractPlugin>

    fun loadPlugins(pluginDirectory: String) {
        plugins = PluginLoader.loadPlugins(File(pluginDirectory))
    }

    fun disablePlugins() {
        plugins.forEach { (name, plugin) ->
            plugin.onDisable()
            plugins.remove(name)
        }
    }

}