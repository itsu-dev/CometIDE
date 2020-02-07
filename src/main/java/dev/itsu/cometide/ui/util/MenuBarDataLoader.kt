package dev.itsu.cometide.ui.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.MenuItemClickedEvent
import dev.itsu.cometide.lang.BaseLang
import dev.itsu.cometide.model.MenuData
import dev.itsu.cometide.plugin.PluginManager
import dev.itsu.cometide.util.IOUtils
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.image.ImageView
import java.util.*

object MenuBarDataLoader {

    fun createMenuBar(menuBar: MenuBar) {
        val menusData = LinkedList<MenuData>()
        val gson = Gson()
        val typeToken = TypeToken.getParameterized(LinkedList::class.java, MenuData::class.java).type
        menusData.addAll(gson.fromJson(IOUtils.readFileFromProject("menu/menu_bar.json"), typeToken))

        PluginManager.plugins.values.forEach {
            if (it.manifest.externalMenu != null) {
                val data: LinkedList<MenuData> = gson.fromJson(IOUtils.readFileFromInputStream(it.classLoader.getResourceAsStream(it.manifest.externalMenu) ?: return@forEach), typeToken)
                data.forEach { children ->
                    val parent = menusData
                            .stream()
                            .filter { it.name == children.name }
                            .findFirst()
                    if (parent.isPresent) {
                        children.items?.forEach {
                            processMenuBar(parent.get(), it)
                        }
                    } else {
                        menusData.add(children)
                    }
                }
            }
        }

        menusData.forEach { menu ->
            val rootMenu = Menu()
            rootMenu.text = BaseLang.getLang(menu.name ?: "null")
            rootMenu.id = menu.name
            createMenu(rootMenu, menu.items)
            menuBar.menus.add(rootMenu)
        }
    }

    private fun createMenu(menu: Menu, data: LinkedList<MenuData>?) {
        data?.forEach {
            if (it.separator == true) {
                menu.items.add(SeparatorMenuItem())

            } else {
                val children = when (it.items) {
                    null -> MenuItem()
                    else -> Menu()
                }

                if (children is Menu) {
                    createMenu(children, it.items)
                }

                children.text = BaseLang.getLang(it.name ?: "")
                children.id = it.name
                if (it.icon != null) children.graphic = ImageView(IconCreator.createImage(it.icon, MenuBarDataLoader::class.java.classLoader))
                children.setOnAction {
                    EventManager.callEvent(MenuItemClickedEvent(children.id))
                }

                menu.items.add(children)
            }
        }
    }

    private fun processMenuBar(parent: MenuData, children: MenuData) {
        val first = (parent.items ?: return)
                .stream()
                .filter { it.getMenuDataByName(children.name ?: return@filter false ) != null }
                .findFirst()
        if (first.isPresent) processMenuBar(first.get().getMenuDataByName(children.name!!)!!, children)
        else parent.items.add(children)
    }


}