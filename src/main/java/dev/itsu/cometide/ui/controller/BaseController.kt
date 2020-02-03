package dev.itsu.cometide.ui.controller

import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.ProjectTreeItemSelectedEvent
import dev.itsu.cometide.event.events.ui.TabPaneTabSelectedEvent
import dev.itsu.cometide.model.ui.BaseDataModel
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.part.pathholder.PathHolder
import dev.itsu.cometide.ui.part.projecttree.ProjectTree
import dev.itsu.cometide.ui.part.tab.TabContent
import dev.itsu.cometide.ui.util.MenuBarDataLoader
import javafx.fxml.FXML
import javafx.scene.control.*
import java.lang.IllegalStateException


class BaseController {
    @FXML
    private lateinit var baseMenuBar: MenuBar

    @FXML
    private lateinit var pathHolder: PathHolder

    @FXML
    private lateinit var horizontalSplitPane: SplitPane

    @FXML
    private lateinit var projectTree: ProjectTree

    @FXML
    private lateinit var mainTabPane: TabPane

    @FXML
    private lateinit var secondTabPane: TabPane

    private var baseDataModel: BaseDataModel? = null

    fun initialize() {
        mainTabPane.selectionModel.selectedItemProperty().addListener {_, _, newValue ->
            if (newValue is TabContent) {
                pathHolder.reload(newValue.treeItemData)
                EventManager.callEvent(TabPaneTabSelectedEvent(newValue.treeItemData, mainTabPane.selectionModel.selectedIndex))
            }
        }

        projectTree.getTreeView().selectionModel.selectedItemProperty().addListener {_, _, newValue ->
            pathHolder.reload(newValue.value)
            EventManager.callEvent(ProjectTreeItemSelectedEvent(newValue.value))
        }

        MenuBarDataLoader.createMenuBar(baseMenuBar)
    }

    fun initializeData(model: BaseDataModel) {
        if (baseDataModel != null) throw IllegalStateException("BaseDataModel has already initialized!")

        baseDataModel = model
        baseDataModel!!.selectingProjectTreeItem.addListener { _, _, newValue -> pathHolder.reload(newValue) }
    }

    fun addToMainTab(tab: TabContent) {
        val tabs = mainTabPane.tabs.stream()
                .filter { if (it is TabContent) { return@filter it.treeItemData.path == tab.treeItemData.path } else return@filter false}
                .toArray()
        if (tabs.isEmpty()) {
            mainTabPane.tabs.add(tab)
        } else {
            // TODO Move to the tab which has same path
        }
    }
}
