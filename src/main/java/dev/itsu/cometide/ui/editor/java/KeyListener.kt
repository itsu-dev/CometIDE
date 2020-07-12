package dev.itsu.cometide.ui.editor.java

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class KeyListener(private val editor: JavaEditorImpl) {

    companion object {
        const val INDENT_TEXT = " "
        const val INDENT_COUNT = 4
    }

    fun onKeyPressed(event: KeyEvent) {
        if (event.isControlDown) {
            onKeyPressedWithCtrl(event)
        }

        if (event.code == KeyCode.ENTER) {
            onEnterPressed()
        }
    }

    fun onKeyReleased(event: KeyEvent) {

    }

    fun onKeyTyped(event: KeyEvent) {

    }

    private fun onEnterPressed() {
        var i = editor.codeArea.currentParagraph

        var text = ""
        while (i >= 0) {
            val previousParagraph = editor.codeArea.paragraphs[i]
            if (previousParagraph.text.isNotEmpty()) {
                text = previousParagraph.text
                break
            }
            i--
        }

        var indentCount = getIndentCount(text)

        if (text.endsWith("}")) {
            indentCount -= INDENT_COUNT
            if (indentCount < 0) indentCount = 0

        } else if (text.endsWith("{")) {
            indentCount += INDENT_COUNT
        }

        var indent = ""
        for (j in 0 until indentCount) {
            indent += INDENT_TEXT
        }

        val current = editor.codeArea.paragraphs[editor.codeArea.currentParagraph]
        if (current.text.isEmpty()) editor.codeArea.insertText(editor.codeArea.caretPosition, indent)
    }

    private fun getIndentCount(text: String): Int {
        var count = 0
        text.forEach {
            if (it.toString() == INDENT_TEXT) count++
            else return count
        }
        return count
    }

    private fun onKeyPressedWithCtrl(event: KeyEvent) {
        when (event.code) {

        }
    }

}