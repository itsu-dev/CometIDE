package dev.itsu.cometide.ui.contentbase.tabpane

import dev.itsu.cometide.ui.UIInterface
import dev.itsu.cometide.ui.contentbase.tabpane.tab.TabImpl
import javafx.collections.ObservableList
import javafx.scene.control.Tab

interface ITabPane {

    interface UI : UIInterface.UI {
        fun addTab(tab: TabImpl)
        fun setTab(name: String)
        fun selectTab(index: Int)
        fun getTabs(): ObservableList<Tab>
    }

    interface Presenter : UIInterface.Presenter {
        fun loadPreviousFiles()
        fun onSelectTab(tab: TabImpl, index: Int)
        fun onCloseTab(tab: TabImpl)
    }

}