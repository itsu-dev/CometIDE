package dev.itsu.cometide.ui.contentbase.tabpane

import dev.itsu.cometide.ui.contentbase.tabpane.tab.TabImpl
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import java.util.stream.Collectors

class TabPaneImpl : ITabPane.UI {

    private val presenter = TabPanePresenter(this)

    private val tabPane = TabPane()

    init {
        tabPane.tabClosingPolicy = TabPane.TabClosingPolicy.ALL_TABS
        tabPane.selectionModel.selectedItemProperty().addListener { _, _, _ ->
            if (tabPane.selectionModel.selectedItem is TabImpl)
                presenter.onSelectTab(tabPane.selectionModel.selectedItem as TabImpl, tabPane.selectionModel.selectedIndex)
        }
    }

    override fun addTab(tab: TabImpl) {
        tabPane.tabs.add(tab)
        setTab(tab.getName())
    }

    override fun setTab(name: String) {
        val tabs = getTabs()
                .stream()
                .filter { it.text == name }
                .collect(Collectors.toList())
        if (tabs.isNotEmpty()) {
            tabPane.selectionModel.select(tabs[0])
        }
    }

    override fun selectTab(index: Int) = tabPane.selectionModel.select(index)

    override fun getTabs(): ObservableList<Tab> = tabPane.tabs

    override fun getContent(): Node = tabPane

    override fun onSceneCreated(scene: Scene) = presenter.loadPreviousFiles()

}