package dev.itsu.cometide.ui.contentbase.bottombar

import dev.itsu.cometide.data.Settings
import dev.itsu.cometide.lang.BaseLang
import dev.itsu.cometide.ui.util.IconCreator
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.*

class BottomBarImpl : IBottomBar.UI {

    private val presenter = BottomBarPresenter(this)

    private val root = GridPane()

    private val left = HBox()
    private val statusLabel = Label(BaseLang.getLang("bottombar.status.ready"))

    private val center = HBox()

    private val right = HBox()
    private val locationLabel = Label("")
    private val encodingLabel = Label(Settings.getInstance().defaultEncode)

    init {
        root.alignment = Pos.CENTER
        root.prefHeight = 30.0

        createLeft()
        createCenter()
        createRight()

        val columnConstraints = ColumnConstraints()
        columnConstraints.percentWidth = 33.3
        columnConstraints.hgrow = Priority.ALWAYS

        root.addColumn(0, left)
        root.addColumn(1, center)
        root.addColumn(2, right)
        root.columnConstraints.addAll(columnConstraints, columnConstraints, columnConstraints)
    }

    override fun setLocation(line: Int, column: Int) {
        locationLabel.text = "$line:$column"
    }

    override fun setStatus(text: String) {
        statusLabel.text = text
    }

    override fun getContent(): Node {
        return root
    }

    override fun onSceneCreated(scene: Scene) {

    }

    override fun createLeft() {
        left.alignment = Pos.CENTER_LEFT
        left.spacing = 8.0

        statusLabel.graphic = ImageView(IconCreator.createImage("img/icon/icon_ready.png"))
        left.children.addAll(statusLabel)
    }

    override fun createRight() {
        right.alignment = Pos.CENTER_RIGHT
        right.spacing = 8.0
        right.children.addAll(encodingLabel, locationLabel)
    }

    override fun createCenter() {
        center.alignment = Pos.CENTER
    }
}