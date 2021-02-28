package dev.itsu.cometide.dao

import org.dom4j.Document
import org.dom4j.io.SAXReader
import org.redundent.kotlin.xml.PrintOptions
import org.redundent.kotlin.xml.xml
import java.io.File

object SettingsDao : XMLDao() {

    lateinit var FALLBACK_LANGUAGE: String
    lateinit var LANGUAGE: String
    lateinit var ENCODING: String
    lateinit var THEME: String
    lateinit var PREVIOUS_PROJECT: String
    var ENVIRONMENT_VARIABLES = mutableMapOf<String, String>()

    init {
        load()
        ENVIRONMENT_VARIABLES.forEach {(key, value) ->
            val file = File(value)
            if (!file.exists()) file.mkdir()
        }
    }

    override fun load() {
        val file = File(getFilePath())
        val document: Document = when (file.exists()) {
            true -> SAXReader().read(file)
            false -> SAXReader().read(SettingsDao::class.java.classLoader.getResource(getFilePath().substring(1)))
        }

        FALLBACK_LANGUAGE = document.selectSingleNode("/settings/fallbackLanguage").stringValue
        LANGUAGE = document.selectSingleNode("/settings/language").stringValue
        ENCODING = document.selectSingleNode("/settings/encoding").stringValue
        THEME = document.selectSingleNode("/settings/theme").stringValue
        PREVIOUS_PROJECT = document.selectSingleNode("/settings/previousProject").stringValue

        val nodes = document.selectNodes("/settings/environmentVariables/variable")
        nodes.forEach {
            ENVIRONMENT_VARIABLES[it.valueOf("@key")] = it.valueOf("@value")
        }
    }

    override fun store() {
        val settingsData = xml("settings") {
            "fallbackLanguage" { -FALLBACK_LANGUAGE }
            "language" { -LANGUAGE }
            "encoding" { -ENCODING }
            "theme" { -THEME }
            "previousProject" { -PREVIOUS_PROJECT }
            "environmentVariables" {
                ENVIRONMENT_VARIABLES.forEach { (key, value) ->
                    "variable" {
                        attribute("key", key)
                        attribute("value", value)
                    }
                }
            }
        }
        store(settingsData.toString(PrintOptions(true, true, false)))
    }

    override fun getFilePath(): String {
        return "./data/settings/settings.xml"
    }

    fun resetProject() {
        PREVIOUS_PROJECT = ""
        store()
    }
}