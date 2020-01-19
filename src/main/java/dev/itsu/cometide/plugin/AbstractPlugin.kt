package dev.itsu.cometide.plugin

import dev.itsu.cometide.data.EnvironmentSettings
import dev.itsu.cometide.lang.PluginLangLoader
import dev.itsu.cometide.plugin.loader.PluginClassLoader
import dev.itsu.cometide.ui.util.ThemeInstaller
import java.io.File

abstract class AbstractPlugin {

    lateinit var manifest: PluginManifest
    lateinit var classLoader: ClassLoader
    lateinit var dataDir: File
    lateinit var file: File
    private var initialized = false

    abstract fun onEnable()

    open fun onDisable() {}

    fun initialize(manifest: PluginManifest, cl: ClassLoader, dataDir: File, file: File) {
        if (!initialized) {
            this.manifest = manifest
            this.classLoader = cl
            this.dataDir = dataDir
            this.file = file
            initialized = true
        }

        if (manifest.externalTheme != null) {
            ThemeInstaller.installTheme(file, manifest.externalTheme!!, this.classLoader, EnvironmentSettings.THEMES_DIRECTORY_PATH)
        }

        if (manifest.externalLanguage != null) {
            PluginLangLoader.load(manifest.externalLanguage!!, file)
        }
    }

}