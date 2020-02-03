package dev.itsu.cometide.ui.part.bottombar

import dev.itsu.cometide.data.Settings
import dev.itsu.cometide.lang.BaseLang
import dev.itsu.cometide.ui.util.IconCreator
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority

class BottomBar : GridPane() {

    private val left = HBox()
    private val statusLabel = Label()

    private val center = HBox()
    private val informationLabel = Label()

    private val right = HBox()
    private val locationLabel = Label()
    private val encodingLabel = Label()
    init {
        alignment = Pos.CENTER
        prefHeight = 30.0

        createLeft()
        createCenter()
        createRight()

        val columnConstraints = ColumnConstraints()
        columnConstraints.percentWidth = 33.3
        columnConstraints.hgrow = Priority.ALWAYS

        addColumn(0, left)
        addColumn(1, center)
        addColumn(2, right)

        this.columnConstraints.addAll(columnConstraints, columnConstraints, columnConstraints)
    }

    private fun createLeft() {
        left.alignment = Pos.CENTER_LEFT
        left.spacing = 8.0

        statusLabel.graphic = ImageView(IconCreator.createImage("img/icon/icon_ready.png"))
        left.children.addAll(statusLabel)
    }

    private fun createRight() {
        right.alignment = Pos.CENTER_RIGHT
        right.spacing = 8.0
        right.children.addAll(encodingLabel, locationLabel)
    }

    private fun createCenter() {
        center.alignment = Pos.CENTER
        center.children.addAll(informationLabel)
    }

    fun setStatus(text: String) {
        statusLabel.text = text
    }

    fun setStatusIcon(image: Image?) {
        if (image != null) {
            statusLabel.graphic = IconCreator.createImageView(image)
        } else {
            statusLabel.graphic = null
        }
    }

    fun setInformation(text: String) {
        informationLabel.text = text
    }

    fun setInformationIcon(image: Image?) {
        if (image != null) {
            informationLabel.graphic = IconCreator.createImageView(image)
        } else {
            informationLabel.graphic = null
        }
    }

    fun setEncoding(text: String) {
        encodingLabel.text = text
    }

    fun setCaretPosition(text: String) {
        locationLabel.text = text
    }
}