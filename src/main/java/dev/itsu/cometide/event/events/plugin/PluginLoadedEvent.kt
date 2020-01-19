package dev.itsu.cometide.event.events.plugin

import dev.itsu.cometide.event.events.Event
import dev.itsu.cometide.plugin.PluginManifest

class PluginLoadedEvent(val manifest: PluginManifest) : Event()