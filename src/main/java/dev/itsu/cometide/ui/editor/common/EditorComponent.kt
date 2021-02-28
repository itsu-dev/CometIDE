package dev.itsu.cometide.ui.editor.common

import com.vladsch.flexmark.util.Mutable
import dev.itsu.cometide.model.TreeItemData
import javafx.concurrent.Worker
import javafx.scene.layout.StackPane
import javafx.scene.web.WebView
import netscape.javascript.JSObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class EditorComponent(val treeItemData: TreeItemData, variables: EditorVariables) : StackPane() {

    val webView = WebView()

    private var onLoad: () -> Unit = {}

    constructor(treeItemData: TreeItemData) : this(treeItemData, EditorVariables.create())

    init {
        this.children.addAll(webView)
        webView.engine.userAgentProperty().value = webView.engine.userAgentProperty().get() + " Chrome/17"
        webView.engine.isJavaScriptEnabled = true

        webView.engine.loadWorker.stateProperty().addListener { ov, oldState, newState ->
            if (newState == Worker.State.SUCCEEDED) {
                setText(File(treeItemData.path).readText(StandardCharsets.UTF_8))
                onLoad.invoke()
            }
        }

        BufferedReader(InputStreamReader(javaClass.classLoader.getResourceAsStream("editor/editor.html"))).use {
            val text = setVariables(it.readText(), variables.values())
            webView.engine.loadContent(text)
        }

    }

    private fun setVariables(source: String, map: MutableMap<String, String>): String {
        var text = source
        if (!map.containsKey("EDITOR_MODE")) map["EDITOR_MODE"] = "ace/mode/${File(treeItemData.path).extension}"
        map.forEach { (key, value) -> text = text.replace("%${key}%", value)}
        return text
    }

    fun setText(text: String) {
        var editor = webView.engine.executeScript("editor") as JSObject
        editor.call("setValue", text)
    }

    fun getText(): String = webView.engine.executeScript("editor.getValue()") as String

    fun setOnLoad(onLoad: () -> Unit) {
        this.onLoad = onLoad
    }
}

class EditorVariables {
    private val map = mutableMapOf<String, String>()

    private constructor()

    init {
        map["EDITOR_THEME"] = "ace/theme/tomorrow_night"
    }

    companion object {
        fun create() = EditorVariables()
    }

    fun setEditorMode(value: String): EditorVariables {
        map["EDITOR_MODE"] = value
        return this
    }

    fun setTheme(value: String): EditorVariables {
        map["EDITOR_THEME"] = value
        return this
    }

    fun values() = map
}