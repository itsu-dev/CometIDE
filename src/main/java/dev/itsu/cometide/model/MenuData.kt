package dev.itsu.cometide.model

import java.util.*
import kotlin.streams.toList

data class MenuData(
        val name: String?,
        val icon: String?,
        val items: LinkedList<MenuData>?,
        val separator: Boolean?
) {

    fun getMenuDataByName(name: String): MenuData? {
        val item = (items ?: return null).stream()
                .filter { it.name == name }
                .toList()
        return if (item.isEmpty()) null else item.first()
    }
}