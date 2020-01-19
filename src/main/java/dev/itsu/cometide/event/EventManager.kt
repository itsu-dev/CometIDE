package dev.itsu.cometide.event

import dev.itsu.cometide.event.events.Event
import dev.itsu.cometide.event.events.EventHandler
import dev.itsu.cometide.event.internal.UIListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventManager {

    private val listeners = mutableListOf<Class<out EventListener>>()

    companion object {
        private var instance: EventManager? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: EventManager().also { instance = it }
        }
    }

    init {
        registerInternalEvents()
    }

    fun registerEvent(listener: Class<out EventListener>) = listeners.add(listener)

    fun callEvent(event: Event) {
        listeners.forEach {clazz ->
            clazz.methods.forEach {
                if (it.isAnnotationPresent(EventHandler::class.java) && it.parameterCount == 1 && it.parameterTypes.contains(event.javaClass)) {
                    it.invoke(clazz.newInstance(), event)
                }
            }
        }
    }

    fun registerInternalEvents() {
        registerEvent(UIListener::class.java)
    }

}