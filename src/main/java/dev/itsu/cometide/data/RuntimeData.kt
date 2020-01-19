package dev.itsu.cometide.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.itsu.cometide.util.PropertyLoader
import dev.itsu.cometide.util.XMLLoader
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.*
import java.lang.Exception
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class RuntimeData {

    var isEmptyProject = true
    var projectRoot: String? = null
    var projectName: String? = null
    var openingFiles: LinkedList<String> = LinkedList()
    var selectingFile: String? = null
    var openingTab: Int = TAB_NOT_SELECTED

    companion object {
        const val TAB_NOT_SELECTED = -1

        const val NAME = "name"
        const val FILE = "file"
        const val TIME_STAMP = "timestamp"

        // previousSession
        const val PROJECT = "project"
        const val PREVIOUS_SESSION = "previousSession"
        const val PROJECT_ROOT = "rootDir"
        const val PROJECT_OPENING_FILES = "openingFiles"
        const val PROJECT_OPENING_TAB_INDEX = "tabIndex"

        private var instance: RuntimeData? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: RuntimeData().also { instance = it }
        }

    }

    fun init() {
        reload()
    }

    fun reload() {
        try {
            val file = File(EnvironmentSettings.TEMP_FILE_PREVIOUS_SESSION_FILE)
            if (!file.exists()) return

            val root = XMLLoader.load(file) ?: return
            val children = root.childNodes
            for (i in 0 until children.length) {
                val parent = children.item(i)
                if (parent.nodeType == Node.ELEMENT_NODE) {
                    processElement(parent as Element)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun processElement(parent: Element) {
        when (parent.tagName) {
            PREVIOUS_SESSION -> processPreviousSession(parent.childNodes)
        }
    }

    private fun processPreviousSession(nodeList: NodeList) {
        for (j in 0 until nodeList.length) {
            val node = nodeList.item(j)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                when (element.tagName) {
                    PROJECT_ROOT -> projectRoot = element.textContent
                    NAME -> projectName = element.textContent
                    PROJECT_OPENING_TAB_INDEX -> openingTab = element.textContent.toInt()
                    PROJECT_OPENING_FILES -> loadOpeningFiles(element)
                }
            }
        }
        isEmptyProject = false
    }

    private fun loadOpeningFiles(element: Element) {
        val nodeList = element.childNodes
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeName == FILE && node.nodeType == Node.ELEMENT_NODE) {
                openingFiles.add(node.textContent)
            }
        }
    }

    fun save() {
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
        val project = document.createElement(PROJECT)
        document.appendChild(project)
        createTimeStamp(project, document)
        createPreviousSession(project, document)
        XMLLoader.write(document, File(EnvironmentSettings.TEMP_FILE_PREVIOUS_SESSION_FILE))
    }

    private fun createTimeStamp(project: Element, document: Document) {
        val timeStamp = document.createElement(TIME_STAMP)
        timeStamp.textContent = System.currentTimeMillis().toString()
        project.appendChild(timeStamp)
    }

    private fun createPreviousSession(project: Element, document: Document) {
        val previousSession = document.createElement(PREVIOUS_SESSION)
        project.appendChild(previousSession)

        if (projectRoot != null) {
            val rootDir = document.createElement(PROJECT_ROOT)
            rootDir.textContent = projectRoot
            previousSession.appendChild(rootDir)
        }

        if (projectName != null) {
            val name = document.createElement(NAME)
            name.textContent = projectName
            previousSession.appendChild(name)
        }

        if (openingTab != TAB_NOT_SELECTED) {
            val tabIndex = document.createElement(PROJECT_OPENING_TAB_INDEX)
            tabIndex.textContent = openingTab.toString()
            previousSession.appendChild(tabIndex)
        }

        if (openingFiles.size != 0) {
            val openingFiles = document.createElement(PROJECT_OPENING_FILES)
            this.openingFiles.forEach {
                val file = document.createElement(FILE)
                file.textContent = it
                openingFiles.appendChild(file)
            }
            previousSession.appendChild(openingFiles)
        }
    }

    fun addOpeningFile(path: String) = openingFiles.add(path)

    fun isOpening(path: String): Boolean = openingFiles.contains(path)

    fun removeOpeningFile(path: String) = openingFiles.remove(path)
}