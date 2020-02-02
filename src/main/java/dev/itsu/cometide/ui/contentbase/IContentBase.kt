package dev.itsu.cometide.ui.contentbase

import dev.itsu.cometide.ui.UIInterface
import dev.itsu.cometide.ui.contentbase.bottombar.BottomBarImpl
import dev.itsu.cometide.ui.contentbase.projecttree.ProjectTreeImpl
import dev.itsu.cometide.ui.contentbase.tabpane.TabPaneImpl
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane

interface IContentBase {

    interface UI : UIInterface.UI {
        fun createLeftPane()
        fun createRightPane()
        fun createBottomPane()
        fun getEditorPane(): TabPaneImpl
        fun getBottomBar(): BottomBarImpl
        fun getProjectTree(): ProjectTreeImpl
    }

    interface Presenter : UIInterface.Presenter {

    }
}