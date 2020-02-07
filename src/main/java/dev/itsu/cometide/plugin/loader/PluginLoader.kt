package dev.itsu.cometide.plugin.loader

import dev.itsu.cometide.dao.SettingsDao
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.plugin.PluginLoadedEvent
import dev.itsu.cometide.plugin.AbstractPlugin
import dev.itsu.cometide.plugin.AbstractPluginEntryPoint
import dev.itsu.cometide.plugin.PluginManifest
import java.io.File

object PluginLoader {

    const val PLUGIN_ENTRY_POINT = "PluginEntryPoint"

    fun loadPlugins(pluginDirectory: File): MutableMap<String, AbstractPlugin> {
        val result = mutableMapOf<String, AbstractPlugin>()
        pluginDirectory.listFiles()?.forEach {
            if (!it.isDirectory && it.extension == "jar") {
                val data = loadPlugin(it) ?: return@forEach
                result[data.first] = data.second
            }
        }
        return result
    }

    fun loadPlugin(target: File): Pair<String, AbstractPlugin>? {
        val manifest: PluginManifest
        try {
            manifest = PluginClassLoader(this::class.java.classLoader, target)
                    .loadClass(PLUGIN_ENTRY_POINT)
                    .asSubclass(AbstractPluginEntryPoint::class.java)
                    .newInstance()
                    .getManifest()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            return null
        }

        var classLoader: PluginClassLoader? = null
        var mainClass: AbstractPlugin? = null
        try {
            classLoader = PluginClassLoader(this::class.java.classLoader, target)
            mainClass = classLoader
                    .loadClass(manifest.mainClass)
                    .asSubclass(AbstractPlugin::class.java)
                    .newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mainClass ?: return null
        mainClass.initialize(manifest, classLoader ?: return null, File("${SettingsDao.ENVIRONMENT_VARIABLES["PLUGINS_DIRECTORY_PATH"]}/${manifest.name}"), target)
        mainClass.onEnable()

        EventManager.callEvent(PluginLoadedEvent(manifest))

        return Pair(manifest.name, mainClass)
    }
}