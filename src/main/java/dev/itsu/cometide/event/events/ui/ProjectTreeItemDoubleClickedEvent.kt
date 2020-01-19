package dev.itsu.cometide.event.events.ui

import dev.itsu.cometide.event.events.Event
import dev.itsu.cometide.model.TreeItemData
import javafx.scene.control.TreeItem
import javafx.scene.input.MouseEvent

class ProjectTreeItemDoubleClickedEvent(
        val mouseEvent: MouseEvent,
        val selectedItem: TreeItem<TreeItemData>
) : Event()