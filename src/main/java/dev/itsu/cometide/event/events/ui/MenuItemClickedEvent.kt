package dev.itsu.cometide.event.events.ui

import dev.itsu.cometide.event.events.Event

class MenuItemClickedEvent(val id: String, val data: MutableMap<String, Any>) : Event() {

    constructor(id: String) : this(id, mutableMapOf())

}