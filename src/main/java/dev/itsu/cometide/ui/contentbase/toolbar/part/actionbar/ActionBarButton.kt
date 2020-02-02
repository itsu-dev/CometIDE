package dev.itsu.cometide.ui.contentbase.toolbar.part.actionbar

import dev.itsu.cometide.ui.util.IconCreator
import javafx.scene.control.Button
import javafx.scene.image.Image

class ActionBarButton(icon: Image) : Button() {

    init {
        graphic = IconCreator.createImageView(icon)
        text = ""
        styleClass.add("action-bar-button")
    }
}