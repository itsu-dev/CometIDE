package dev.itsu.cometide.event.events.ui

import dev.itsu.cometide.event.events.Event
import dev.itsu.cometide.model.TreeItemData

class TabPaneTabSelectedEvent(
        val data: TreeItemData,
        val index: Int
) : Event()