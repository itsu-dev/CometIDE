package dev.itsu.cometide.ui.contentbase.bottombar

import dev.itsu.cometide.ui.UIInterface

interface IBottomBar {

    interface UI : UIInterface.UI {
        fun setLocation(line: Int, column: Int)
        fun setStatus(text: String)
        fun createLeft()
        fun createRight()
        fun createCenter()
    }

    interface Presenter : UIInterface.Presenter {
        fun setLocation(line: Int, column: Int)
        fun setStatus(text: String)
    }

}