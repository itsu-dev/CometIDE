package dev.itsu.cometide.ui.contentbase.menubar

import dev.itsu.cometide.ui.UIInterface
import dev.itsu.cometide.model.MenuData
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import java.util.*

interface IMenuBar {

    interface UI : UIInterface.UI {

    }

    interface Presenter : UIInterface.Presenter {
        fun createMenuBar()
        fun createMenu(menu: Menu, data: LinkedList<MenuData>?)
        fun processMenuBar(parent: MenuData, children: MenuData)
    }

}