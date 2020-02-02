package dev.itsu.cometide.ui.contentbase.tabpane.tab

import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.TabPaneTabClosedEvent


class TabPresenter(val tabImpl: TabImpl) : ITab.Presenter {

    override fun onTabClose(tab: TabImpl) {
        ProjectData.removeOpeningFile(tab.treeItemData.path)
        EventManager.getInstance().callEvent(TabPaneTabClosedEvent(tab.treeItemData))
    }

}