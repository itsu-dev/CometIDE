package dev.itsu.cometide.data

import java.io.File

object EnvironmentSettings {

    const val SETTINGS_DIRECTORY_PATH = "./data/settings"
    const val SETTINGS_FILE_PATH = "$SETTINGS_DIRECTORY_PATH/Settings.properties"
    const val TEMP_DIRECTORY_PATH = "./data/temp"
    const val TEMP_FILE_PREVIOUS_SESSION_FILE = "$TEMP_DIRECTORY_PATH/project.xml"
    const val PLUGINS_DIRECTORY_PATH = "./data/plugins"
    const val THEMES_DIRECTORY_PATH = "./data/themes"
    const val THEMES_DIRECTORY_CODES_PATH = "$THEMES_DIRECTORY_PATH/codes"
    const val THEMES_DEFAULT = THEMES_DIRECTORY_PATH + "dark"
    const val FALLBACK_LANGUAGE = "jpn"

    init {
        val settings = File(SETTINGS_DIRECTORY_PATH)
        if (!settings.exists()) settings.mkdirs()

        val temp = File(TEMP_DIRECTORY_PATH)
        if (!temp.exists()) temp.mkdirs()

        val themes = File(THEMES_DIRECTORY_PATH)
        if (!themes.exists()) themes.mkdirs()

        val plugins = File(PLUGINS_DIRECTORY_PATH)
        if (!plugins.exists()) plugins.mkdirs()
    }

}