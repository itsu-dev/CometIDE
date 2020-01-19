package dev.itsu.cometide.ui.contentbase.menubar

import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.SeparatorMenuItem

class MenuBarImpl : IMenuBar.UI {

    private val presenter = MenuBarPresenter(this)

    private val menuBar = MenuBar()

    init {
        presenter.createMenuBar()
    }

    override fun getContent(): Node = menuBar

    override fun onSceneCreated(scene: Scene) {

    }
}