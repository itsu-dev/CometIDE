package dev.itsu.cometide.ui.controller

import dev.itsu.cometide.dao.ProjectDao
import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.ProjectTreeItemDoubleClickedEvent
import dev.itsu.cometide.event.events.ui.ProjectTreeItemRightClickedEvent
import dev.itsu.cometide.event.events.ui.ProjectTreeItemSelectedEvent
import dev.itsu.cometide.event.events.ui.TabPaneTabSelectedEvent
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.model.ui.BaseDataModel
import dev.itsu.cometide.model.ui.BottomBarDataModel
import dev.itsu.cometide.project.ProjectManager
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.part.actionbar.ActionBar
import dev.itsu.cometide.ui.part.pathholder.PathHolder
import dev.itsu.cometide.ui.part.projecttree.ProjectTree
import dev.itsu.cometide.ui.part.tab.TabContent
import dev.itsu.cometide.ui.util.MenuBarDataLoader
import dev.itsu.cometide.util.TextUtils
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import java.lang.IllegalStateException


class BaseController {
    @FXML
    private lateinit var baseMenuBar: MenuBar

    @FXML
    private lateinit var pathHolder: PathHolder

    @FXML
    private lateinit var actionBar: ActionBar

    @FXML
    private lateinit var horizontalSplitPane: SplitPane

    @FXML
    private lateinit var projectTree: ProjectTree

    @FXML
    private lateinit var mainTabPane: TabPane

    @FXML
    private lateinit var secondTabPane: TabPane

    @FXML
    private lateinit var bottomBarController: BottomBarController

    private var baseDataModel: BaseDataModel? = null

    fun initialize() {
        mainTabPane.selectionModel.selectedItemProperty().addListener {_, _, newValue ->
            if (newValue is TabContent) {
                pathHolder.reload(newValue.treeItemData)
                UIManager.setTitle("${ProjectDao.project.name} [${ProjectDao.project.root}] - ${TextUtils.shortenProjectPath(newValue.treeItemData.path)} - CometIDE")
                EventManager.callEvent(TabPaneTabSelectedEvent(newValue.treeItemData, mainTabPane.selectionModel.selectedIndex))
            }
        }

        projectTree.getTreeView().selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            pathHolder.reload(newValue.value)
            EventManager.callEvent(ProjectTreeItemSelectedEvent(newValue.value))
        }

        projectTree.getTreeView().setOnMouseClicked {
            if (it.isSecondaryButtonDown) {
                EventManager.callEvent(ProjectTreeItemRightClickedEvent(it, projectTree.getTreeView().selectionModel.selectedItem))
                return@setOnMouseClicked
            }

            when (it.clickCount) {
                2 -> {
                    ProjectManager.openFile(projectTree.getTreeView().selectionModel.selectedItem.value)
                    EventManager.callEvent(ProjectTreeItemDoubleClickedEvent(it, projectTree.getTreeView().selectionModel.selectedItem))
                }
            }
        }

        MenuBarDataLoader.createMenuBar(baseMenuBar)

        bottomBarController.initializeData(BottomBarDataModel())
        UIManager.setBottomBarController(bottomBarController)
    }

    fun initializeData(model: BaseDataModel) {
        if (baseDataModel != null) throw IllegalStateException("BaseDataModel has already initialized!")

        baseDataModel = model
        baseDataModel!!.selectingProjectTreeItem.addListener { _, _, newValue -> GlobalScope.launch(Dispatchers.JavaFx) { pathHolder.reload(newValue) } }
        baseDataModel!!.projectRoot.addListener { _, _, newValue ->  projectTree.reload(newValue) }
        baseDataModel!!.openingProjects.addListener(ListChangeListener {
            while(it.next()) {
                if (it.wasAdded()) {
                    addToMainTab(ProjectManager.getTab(it.list[it.to]))
                }
            }
        })
    }

    fun addToMainTab(tab: TabContent) {
        val tabs = mainTabPane.tabs.stream()
                .filter { if (it is TabContent) { return@filter it.treeItemData.path == tab.treeItemData.path } else return@filter false}
                .toArray()
        if (tabs.isEmpty()) mainTabPane.tabs.add(tab)

        setTab(tab.treeItemData)
    }

    fun setTab(treeItemData: TreeItemData) {
        val tabs = mainTabPane.tabs.stream()
                .filter { if (it is TabContent) { return@filter it.treeItemData.path == treeItemData.path } else return@filter false}
                .toArray()
        if (tabs.isNotEmpty()) mainTabPane.selectionModel.select(tabs[0] as Tab)
    }

    fun setTab(index: Int) {
        mainTabPane.selectionModel.select(index)
    }

    fun getDataModel(): BaseDataModel = baseDataModel!!
}
