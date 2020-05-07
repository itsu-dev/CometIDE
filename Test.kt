package com.saisana299.test

import cn.nukkit.plugin.PluginBase
import java.awt.EventQueue
import javax.swing.UIManager

import com.formdev.flatlaf.FlatLightLaf

class Test: PluginBase() {
    private lateinit var testwindow: TestWindow
    override fun onEnable() {
        EventQueue.invokeLater {
            try {
                UIManager.setLookAndFeel(FlatLightLaf())
                testwindow = TestWindow(this)
                testwindow.setVisible(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}