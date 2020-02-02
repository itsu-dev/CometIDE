package dev.itsu.cometide.ui.contentbase.tabpane.tab

import dev.itsu.cometide.data.EditorData
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.util.IconCreator
import dev.itsu.cometide.ui.contentbase.tabcontent.ITabContent
import javafx.event.EventHandler
import javafx.scene.control.Tab

class TabImpl(val treeItemData: TreeItemData) : ITab.UI, Tab() {

    private val presenter = TabPresenter(this)

    private val tab = Tab()
    private val tabContent: ITabContent = EditorData.getTabContent(treeItemData)!!

    init {
        this.text = treeItemData.name
        this.content = tabContent.getContent()
        this.graphic = IconCreator.createImageFromData(treeItemData)
        this.onClosed = EventHandler { presenter.onTabClose(this) }
    }

    override fun getName(): String = treeItemData.name

    override fun getData(): TreeItemData = treeItemData

    override fun getTabContent(): ITabContent = tabContent

    override fun getTab(): Tab  = tab
}