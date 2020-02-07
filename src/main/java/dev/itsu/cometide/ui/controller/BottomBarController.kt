package dev.itsu.cometide.ui.controller

import com.jfoenix.controls.JFXProgressBar
import dev.itsu.cometide.dao.SettingsDao
import dev.itsu.cometide.lang.BaseLang
import dev.itsu.cometide.model.ui.BottomBarDataModel
import dev.itsu.cometide.ui.util.IconCreator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.util.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch


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
        bottomBarDataModel!!.status.addListener { _, _, newValue ->
            GlobalScope.launch(Dispatchers.JavaFx) {
                statusLabel.text = newValue
                if (newValue == BaseLang.getLang("bottombar.status.ready")) {
                    statusLabel.graphic = IconCreator.createImageView(IconCreator.createImage("img/icon/icon_ready.png"))
                }
            }
        }
        bottomBarDataModel!!.information.addListener { _, _, newValue -> GlobalScope.launch(Dispatchers.JavaFx) { informationLabel.text = newValue } }
        bottomBarDataModel!!.encoding.addListener { _, _, newValue -> GlobalScope.launch(Dispatchers.JavaFx) { encodingLabel.text = newValue } }
        bottomBarDataModel!!.caretPosition.addListener { _, _, newValue -> GlobalScope.launch(Dispatchers.JavaFx) { caretPosLabel.text = newValue } }

        bottomBarDataModel!!.setEncoding(SettingsDao.ENCODING)
    }

    fun setLoading(boolean: Boolean) {
        when (boolean) {
            true -> {
                val indicator = JFXProgressBar()
                indicator.progress = -1.0
                informationLabel.graphic = indicator
            }
            false -> informationLabel.graphic = null
        }
    }

    fun getDataModel(): BottomBarDataModel = bottomBarDataModel!!

}
