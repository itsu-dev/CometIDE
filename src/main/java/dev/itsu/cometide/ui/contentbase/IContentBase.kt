package dev.itsu.cometide.ui.contentbase

import dev.itsu.cometide.ui.UIInterface
import dev.itsu.cometide.ui.contentbase.tabpane.TabPaneImpl
import javafx.scene.layout.StackPane

interface IContentBase {

    interface UI : UIInterface.UI {
        fun createLeftPane()
        fun createRightPane()
        fun createBottomPane()
        fun getLeftPane(): StackPane
        fun getRightPane(): StackPane
        fun getBottomPane(): StackPane
        fun getEditorPane(): TabPaneImpl
    }

    interface Presenter : UIInterface.Presenter {

    }
}