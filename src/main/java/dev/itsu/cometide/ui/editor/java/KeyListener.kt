package dev.itsu.cometide.ui.editor.java

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.printer.PrettyPrinterConfiguration
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class KeyListener(private val editor: JavaEditorImpl) {

    companion object {
        const val INDENT_TEXT = " "
        const val INDENT_COUNT = 4
    }

    fun onKeyPressed(event: KeyEvent) {
        if (event.isControlDown && event.isAltDown) {
            onKeyPressedWithCtrlAndAlt(event)
        }

        if (event.code == KeyCode.ENTER) {
            onEnterPressed()
        }

        if (event.code == KeyCode.BACK_SPACE) {
            onBackspacePressed()
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

    private fun onBackspacePressed() {
        val current = editor.codeArea.paragraphs[editor.codeArea.currentParagraph] ?: return
        if (current.text.isEmpty()) return

        if (current.text.replace(" ", "").isEmpty()) {
            val indentCount = getIndentCount(current.text)
            editor.codeArea.replaceText(
                    editor.codeArea.currentParagraph,
                    editor.codeArea.caretColumn - indentCount,
                    editor.codeArea.currentParagraph,
                    editor.codeArea.caretColumn,
                    ""
            )
            editor.codeArea.moveTo(editor.codeArea.caretPosition - 1)
            editor.codeArea.deleteNextChar()
        }
    }

    private fun onKeyPressedWithCtrlAndAlt(event: KeyEvent) {
        when (event.code) {
           KeyCode.L -> {
               val conf = PrettyPrinterConfiguration()
               conf.indentSize = INDENT_COUNT
               conf.indentType = PrettyPrinterConfiguration.IndentType.SPACES
               conf.isPrintComments = true
               conf.isPrintJavadoc = true
               editor.codeArea.replaceText(JavaParser().parse(editor.text).result.get().toString(conf))
           }
        }
    }

}