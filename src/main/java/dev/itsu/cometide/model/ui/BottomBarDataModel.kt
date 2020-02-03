package dev.itsu.cometide.model.ui

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

class BottomBarDataModel {

    val status : StringProperty = SimpleStringProperty("")
    val information: StringProperty = SimpleStringProperty("")
    val encoding: StringProperty = SimpleStringProperty("")
    val caretPosition: StringProperty = SimpleStringProperty("")

    fun setStatus(value: String) = status.set(value)
    fun setInformation(value: String) = information.set(value)
    fun setEncoding(value: String) = encoding.set(value)
    fun setCaretPosition(value: String) = caretPosition.set(value)

}