package dev.itsu.cometide.dao

import org.dom4j.Document
import org.dom4j.io.SAXReader
import org.redundent.kotlin.xml.PrintOptions
import org.redundent.kotlin.xml.xml
import java.io.File

object ExtensionCorrespondsDao : XMLDao() {

    private val extensions: MutableMap<String, String> by lazy { mutableMapOf<String, String>() }
    private val defaultEditor = "dev.itsu.cometide.ui.editor.text.TextEditorImpl"

    init {
        load()
    }

    fun add(name: String, clazz: String) {
        extensions[name] = clazz
    }

    fun getClassByExtension(name: String): String {
        extensions.forEach { (key, value) ->
            if (key.contains(name)) return value
        }
        return defaultEditor
    }

    override fun load() {
        val file = File(getFilePath())
        val document: Document = when (file.exists()) {
            true -> SAXReader().read(file)
            false -> SAXReader().read(ExtensionCorrespondsDao::class.java.classLoader.getResource(getFilePath().substring(1)))
        }
        val nodes = document.selectNodes("/extensionCorresponds/extension")
        nodes.forEach {
            extensions[it.valueOf("@name")] = it.valueOf("@class")
        }
    }

    override fun store() {
        val extensionsData = xml("extensionCorresponds") {
            extensions.forEach { (key, value) ->
                "extension" {
                    attribute("name", key)
                    attribute("class", value)
                }
            }
        }
        store(extensionsData.toString(PrintOptions(true, true, false)))
    }

    override fun getFilePath(): String {
        return "./data/settings/extension_corresponds.xml"
    }

}