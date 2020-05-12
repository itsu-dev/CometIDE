package dev.itsu.cometide.dao

import dev.itsu.cometide.dao.model.Project
import java.io.File
import javax.xml.bind.JAXB

object ProjectDao : XMLDao() {

    lateinit var project: Project
    private var xmlPath: String = SettingsDao.PREVIOUS_PROJECT

    init {
        load()
    }

    override fun store() {
        JAXB.marshal(project, getFilePath())
    }

    override fun load() {
        val file = File(getFilePath())

        this.project = when(file.exists()) {
            true -> JAXB.unmarshal(file, Project::class.java)
            false -> Project()
        }
    }

    override fun getFilePath(): String {
        return xmlPath + "/.comet/project.xml"
    }

    fun reload(path: String) {
        this.xmlPath = path
        load()
    }
}