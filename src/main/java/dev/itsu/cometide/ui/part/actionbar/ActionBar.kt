package dev.itsu.cometide.ui.part.actionbar

import dev.itsu.cometide.event.EventManager
import dev.itsu.cometide.event.events.ui.MenuItemClickedEvent
import dev.itsu.cometide.ui.util.IconCreator
import javafx.geometry.Pos
import javafx.scene.layout.HBox

class ActionBar : HBox() {

    init {
        styleClass.add("actionbar")
        alignment = Pos.CENTER
        prefHeight = 36.0

        val find = ActionBarButton(IconCreator.createImage("img/icon/icon_find.png")).also {
            it.setOnMouseClicked { EventManager.callEvent(MenuItemClickedEvent("menubar.edit.find")) }
        }

        val projectSetting = ActionBarButton(IconCreator.createImage("img/icon/icon_project_setting.png")).also {
            it.setOnMouseClicked { EventManager.callEvent(MenuItemClickedEvent("menubar.file.project_setting")) }
        }

        val screenshot = ActionBarButton(IconCreator.createImage("img/icon/icon_screenshot.png")).also {
            it.setOnMouseClicked { EventManager.callEvent(MenuItemClickedEvent("actionbar.screenshot")) }
        }

        children.addAll(find, projectSetting, screenshot)
    }

}