package dev.itsu.cometide.ui.contentbase.tabpane.tab

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.contentbase.tabcontent.ITabContent
import javafx.scene.control.Tab

interface ITab {

    interface UI {
        fun getName(): String
        fun getData(): TreeItemData
        fun getTabContent(): ITabContent
        fun getTab(): Tab
    }

    interface Presenter {
        fun onTabClose(tab: TabImpl)
    }

}