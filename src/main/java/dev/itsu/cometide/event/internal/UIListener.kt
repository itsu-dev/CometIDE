package dev.itsu.cometide.event.internal

import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.data.Settings
import dev.itsu.cometide.event.EventListener
import dev.itsu.cometide.event.events.*
import dev.itsu.cometide.event.events.plugin.PluginLoadedEvent
import dev.itsu.cometide.event.events.ui.MainStageClosedEvent
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.contentbase.tabpane.tab.TabImpl
import dev.itsu.cometide.ui.editor.AbstractEditor
import kotlin.system.exitProcess

class UIListener : EventListener {

    @EventHandler
    fun onClose(event: MainStageClosedEvent) {
        //PluginManager.getInstance().disablePlugins()

        UIManager.getInstance().splitPane.getEditorPane().getTabs().forEach {
            if (it is TabImpl && it.getTabContent() is AbstractEditor) {
                (it.getTabContent() as AbstractEditor).onClose()
            }
        }
        ProjectData.save()
        Settings.save()

        exitProcess(0)
    }

    @EventHandler
    fun onPluginLoaded(event: PluginLoadedEvent) {
        println("Loaded: ${event.manifest.name}")
    }

}