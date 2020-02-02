package dev.itsu.cometide.ui.contentbase.tabpane

import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.TabPaneTabClosedEvent
import dev.itsu.cometide.event.events.ui.TabPaneTabSelectedEvent
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.contentbase.tabpane.tab.TabImpl
import dev.itsu.cometide.util.TextUtils
import java.io.File

class TabPanePresenter(val tabPaneImpl: TabPaneImpl) : ITabPane.Presenter {

    override fun loadPreviousFiles() {
        ProjectData.getInstance().project.openingFiles.forEach {
            val file = File(it)
            if (file.exists()) {
                UIManager.getInstance().openFile(TreeItemData(file.name, file.absolutePath, false, TreeItemData.Type.ITEM))
            }
        }
        if (ProjectData.getInstance().project.openingTab != ProjectData.TAB_NOT_SELECTED) {
            tabPaneImpl.selectTab(ProjectData.getInstance().project.openingTab)//TODO this does not work.
        }
    }

    override fun onSelectTab(tab: TabImpl, index: Int) {
        ProjectData.getInstance().project.openingTab = index
        UIManager.getInstance().toolBar.reload(tab.treeItemData)

        val runtimeData = ProjectData.getInstance()
        UIManager.getInstance().setTitle("${runtimeData.project.name} [${runtimeData.project.root}] - ${TextUtils.shortenProjectPath(tab.treeItemData.path)} - CometIDE")

        EventManager.getInstance().callEvent(TabPaneTabSelectedEvent(tab.treeItemData, index))
    }

    override fun onCloseTab(tab: TabImpl) {
        ProjectData.getInstance().removeOpeningFile(tab.treeItemData.path)
        EventManager.getInstance().callEvent(TabPaneTabClosedEvent(tab.treeItemData))
    }

}