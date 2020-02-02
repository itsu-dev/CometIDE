package dev.itsu.cometide.event.internal

import dev.itsu.cometide.data.EditorData
import dev.itsu.cometide.event.EventListener
import dev.itsu.cometide.event.events.EventHandler
import dev.itsu.cometide.event.events.ui.MenuItemClickedEvent

class EditMenuListener : EventListener {

    @EventHandler
    fun onMenuClicked(event: MenuItemClickedEvent) {
        when(event.id) {
            "menubar.edit.undo" -> EditorData.currentEditor?.undo()
            "menubar.edit.redo" -> EditorData.currentEditor?.redo()
            "menubar.edit.copy" -> EditorData.currentEditor?.copy()
            "menubar.edit.paste" -> EditorData.currentEditor?.paste()
            "menubar.edit.cut" -> EditorData.currentEditor?.cut()
        }
    }
}