package dev.itsu.cometide.ui

import javafx.scene.Node
import javafx.scene.Scene

interface UIInterface {

    interface UI {
        fun getContent(): Node
        fun onSceneCreated(scene: Scene)
    }

    interface Presenter {

    }

}