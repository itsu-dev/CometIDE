package dev.itsu.cometide.project

import dev.itsu.cometide.dao.ExtensionCorrespondsDao
import dev.itsu.cometide.dao.ProjectDao
import dev.itsu.cometide.dao.SettingsDao
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.part.tab.TabContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import java.io.File

object ProjectManager {

    fun openProject(projectRoot: String) {
        UIManager.getBottomBarController().getDataModel().setInformation("Loading Project...")
        UIManager.getBottomBarController().setLoading(true)

        if (!File(projectRoot).exists()) {
            UIManager.getBottomBarController().getDataModel().setStatus("Failed to load project: $projectRoot")
            UIManager.getBottomBarController().getDataModel().setInformation("")
            UIManager.getBottomBarController().setLoading(false)
            SettingsDao.resetProject()
            return
        }

        GlobalScope.launch(Dispatchers.JavaFx) {
            UIManager.getBaseController().getDataModel().setProjectRoot(projectRoot)
            ProjectDao.reload(projectRoot)
            ProjectDao.project.previousSession.tabs.forEach {
                val file = File(it.filePath)
                if (!file.exists()) return@forEach
                openFile(TreeItemData(file.name, file.absolutePath, false, TreeItemData.Type.ITEM))
            }
            UIManager.getBaseController().setTab(ProjectDao.project.previousSession.tabIndex)
            UIManager.getBottomBarController().getDataModel().setInformation("")
            UIManager.getBottomBarController().setLoading(false)
        }
    }

    fun openFile(treeItemData: TreeItemData) {
        if (treeItemData.type != TreeItemData.Type.GROUP) {
            if (!UIManager.getBaseController().getDataModel().isOpeningProject(treeItemData)) {
                UIManager.getBaseController().addToMainTab(getTab(treeItemData))
                //UIManager.getBaseController().getDataModel().openingProjects.add(treeItemData)
            } else {
                UIManager.getBaseController().setTab(treeItemData)
            }
        }
    }

    fun getTab(treeItemData: TreeItemData): TabContent {
        val clazz = javaClass.classLoader.loadClass(ExtensionCorrespondsDao.getClassByExtension(File(treeItemData.path).extension)).asSubclass(TabContent::class.java)
        return clazz.getConstructor(TreeItemData::class.java).newInstance(treeItemData)
    }

}