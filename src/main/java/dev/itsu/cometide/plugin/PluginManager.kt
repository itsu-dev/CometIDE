package dev.itsu.cometide.plugin

import dev.itsu.cometide.plugin.loader.PluginLoader
import java.io.File

class PluginManager {

    lateinit var plugins: MutableMap<String, AbstractPlugin>

    companion object {
        private var instance: PluginManager? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: PluginManager().also { instance = it }
        }
    }

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