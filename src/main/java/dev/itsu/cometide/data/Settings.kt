package dev.itsu.cometide.data

import dev.itsu.cometide.util.PropertyLoader
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.*

object Settings {

    var language: String
    var fallbackLanguage : String
    var theme: String
    var defaultEncode: String

    const val LANGUAGE = "language"
    const val FALLBACK_LANGUAGE = "fallback_language"
    const val THEME = "theme"
    const val DEFAULT_ENCODE = "default_encode"

    init {
        var properties = PropertyLoader.loadProperties(Settings::class.java.classLoader.getResourceAsStream("settings/Settings.properties"), EnvironmentSettings.SETTINGS_FILE_PATH)
        if (properties == null) {
            properties = Properties()
            properties[LANGUAGE] = "jpn"
            properties[FALLBACK_LANGUAGE] = "eng"
            properties[THEME] = "dark"
            properties[DEFAULT_ENCODE] = "UTF-8"
        }

        language = properties.getProperty(LANGUAGE) ?: "jpn"
        fallbackLanguage = properties.getProperty(FALLBACK_LANGUAGE) ?: "eng"
        theme = properties.getProperty(THEME) ?: "dark"
        defaultEncode = properties.getProperty(DEFAULT_ENCODE) ?: "UTF-8"
    }

    fun save() {
        val properties = Properties()
        properties[LANGUAGE] = language
        properties[FALLBACK_LANGUAGE] = fallbackLanguage
        properties[THEME] = theme
        properties[DEFAULT_ENCODE] = defaultEncode
        properties.store(PrintWriter(FileWriter(File(EnvironmentSettings.SETTINGS_FILE_PATH))), "CometIDE System settings file.")
    }

}