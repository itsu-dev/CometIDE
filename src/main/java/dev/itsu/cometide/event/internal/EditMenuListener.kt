package dev.itsu.cometide.event.internal

import dev.itsu.cometide.data.EditorData
import dev.itsu.cometide.event.EventListener
import dev.itsu.cometide.event.events.EventHandler
import dev.itsu.cometide.event.events.ui.MenuItemClickedEvent

class EditMenuListener : EventListener {

    @EventHandler
    fun onMenuClicked(event: MenuItemClickedEvent) {
        when(event.id) {
            "menubar.edit.undo" -> EditorData.getInstance().currentEditor?.undo()
            "menubar.edit.redo" -> EditorData.getInstance().currentEditor?.redo()
            "menubar.edit.copy" -> EditorData.getInstance().currentEditor?.copy()
            "menubar.edit.paste" -> EditorData.getInstance().currentEditor?.paste()
            "menubar.edit.cut" -> EditorData.getInstance().currentEditor?.cut()
        }
    }
}