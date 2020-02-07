package dev.itsu.cometide.event.internal

import dev.itsu.cometide.dao.ExtensionCorrespondsDao
import dev.itsu.cometide.dao.ProjectDao
import dev.itsu.cometide.dao.SettingsDao
import dev.itsu.cometide.event.EventListener
import dev.itsu.cometide.event.events.*
import dev.itsu.cometide.event.events.plugin.PluginLoadedEvent
import dev.itsu.cometide.event.events.ui.MainStageClosedEvent
import dev.itsu.cometide.event.events.ui.UICreatedEvent
import dev.itsu.cometide.project.ProjectManager
import kotlin.system.exitProcess

class UIListener : EventListener {

    @EventHandler
    fun onClose(event: MainStageClosedEvent) {
        ExtensionCorrespondsDao.store()
        ProjectDao.store()
        SettingsDao.store()

        exitProcess(0)
    }

    @EventHandler
    fun onPluginLoaded(event: PluginLoadedEvent) {
        println("Loaded: ${event.manifest.name}")
    }

    @EventHandler
    fun onUICreated(event: UICreatedEvent) {
        ProjectManager.openProject(SettingsDao.PREVIOUS_PROJECT)
    }

}