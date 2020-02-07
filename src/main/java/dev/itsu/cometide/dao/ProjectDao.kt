package dev.itsu.cometide.dao

import dev.itsu.cometide.project.Project
import org.dom4j.Document
import org.dom4j.io.SAXReader
import org.redundent.kotlin.xml.PrintOptions
import org.redundent.kotlin.xml.xml
import java.io.File

object ProjectDao : XMLDao() {

    lateinit var project: Project
    private var xmlPath: String = SettingsDao.PREVIOUS_PROJECT

    init {
        load()
    }

    override fun store() {
        val projectData = xml("project") {
            "timeStamp" { -System.currentTimeMillis().toString() }
            "previousSession" {
                "rootDir" { -project.root }
                "name" { -project.name }
                "tabIndex" { -project.openingTab.toString() }
                "openingFiles" {
                    project.openingFiles.forEach {
                        "file" { attribute("path", it) }
                    }
                }
            }
        }
        store(projectData.toString(PrintOptions(true, true, false)))
    }

    override fun load() {
        val file = File(getFilePath())
        val document: Document = when (file.exists()) {
            true -> SAXReader().read(file)
            false -> SAXReader().read(ExtensionCorrespondsDao::class.java.classLoader.getResource(getFilePath().substring(1)))
        }

        val nodes = document.selectNodes("/project/previousSession/openingFiles/file")
        val files = mutableListOf<String>()
        nodes.forEach {
            files.add(it.stringValue)
        }

        this.project = Project(
                document.selectSingleNode("/project/previousSession/rootDir").stringValue,
                document.selectSingleNode("/project/previousSession/name").stringValue,
                files,
                "",
                document.selectSingleNode("/project/previousSession/tabIndex").stringValue.toInt()
        )

    }

    override fun getFilePath(): String {
        return xmlPath + "/.comet/project.xml"
    }

    fun reload(path: String) {
        this.xmlPath = path
        load()
    }
}