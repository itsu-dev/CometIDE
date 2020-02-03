package dev.itsu.cometide.ui.controller

import dev.itsu.cometide.model.ui.BottomBarDataModel
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import java.lang.IllegalStateException


class BottomBarController {

    @FXML
    private lateinit var bottomLeftBox: HBox

    @FXML
    private lateinit var statusLabel: Label

    @FXML
    private lateinit var bottomCenterBox: HBox

    @FXML
    private lateinit var informationLabel: Label

    @FXML
    private lateinit var bottomRightBox: HBox

    @FXML
    private lateinit var encodingLabel: Label

    @FXML
    private lateinit var caretPosLabel: Label

    private var bottomBarDataModel: BottomBarDataModel? = null

    fun initializeData(model: BottomBarDataModel) {
        if (bottomBarDataModel != null) throw IllegalStateException("BottomBarDataModel has already initialized!")

        bottomBarDataModel = model
        bottomBarDataModel!!.status.addListener { _, _, newValue -> statusLabel.text = newValue }
        bottomBarDataModel!!.information.addListener { _, _, newValue -> informationLabel.text = newValue }
        bottomBarDataModel!!.encoding.addListener { _, _, newValue -> encodingLabel.text = newValue }
        bottomBarDataModel!!.caretPosition.addListener { _, _, newValue -> caretPosLabel.text = newValue }
    }

    fun setStatus(text: String) = bottomBarDataModel?.setStatus(text)
    fun setInformation(text: String) = bottomBarDataModel?.setInformation(text)
    fun setEncoding(text: String) = bottomBarDataModel?.setEncoding(text)
    fun setCaretPosition(text: String) = bottomBarDataModel?.setCaretPosition(text)
}
