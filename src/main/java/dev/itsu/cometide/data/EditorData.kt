package dev.itsu.cometide.data

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.contentbase.tabcontent.ITabContent
import dev.itsu.cometide.ui.editor.java.JavaEditorImpl
import dev.itsu.cometide.ui.editor.text.TextEditorImpl
import dev.itsu.cometide.ui.contentbase.tabcontent.photograph.PhotoViewer
import dev.itsu.cometide.ui.editor.AbstractEditor
import java.io.File

object EditorData {

    private val editors = mutableMapOf<String, Class<out ITabContent>>()
    var currentEditor: AbstractEditor? = null

    fun getTabContent(treeItemData: TreeItemData): ITabContent? = getTabContent(File(treeItemData.path).extension, treeItemData)

    fun getTabContent(extension: String, treeItemData: TreeItemData): ITabContent? {
        val clazz = editors[extension] ?: editors["txt"] ?: return null
        return clazz.getConstructor(TreeItemData::class.java).newInstance(treeItemData)
    }

    init {
        registerEditor("txt", TextEditorImpl::class.java)
        registerEditor("java", JavaEditorImpl::class.java)
        registerEditor("jpg", PhotoViewer::class.java)
        registerEditor("png", PhotoViewer::class.java)
    }

    fun registerEditor(extension: String, editor: Class<out ITabContent>) {
        editors[extension] = editor
    }
}