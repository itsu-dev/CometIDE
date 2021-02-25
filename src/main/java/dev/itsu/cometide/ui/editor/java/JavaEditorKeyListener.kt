package dev.itsu.cometide.ui.editor.java

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.printer.PrettyPrinterConfiguration
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class JavaEditorKeyListener(private val editor: JavaEditorImpl) {

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

        if (event.code == KeyCode.TAB) {
            event.consume()
            editor.codeArea.deletePreviousChar()
            editor.codeArea.insertText(editor.codeArea.caretPosition, JavaEditorImpl.INDENT)
        }
    }

    private fun onEnterPressed() {
        editor.doIndent()
    }

    private fun onBackspacePressed() {
        editor.doSmartIndentRemoval()
    }

    private fun onKeyPressedWithCtrlAndAlt(event: KeyEvent) {
        when (event.code) {
           KeyCode.L -> {
               val conf = PrettyPrinterConfiguration()
               conf.indentSize = JavaEditorImpl.INDENT_COUNT
               conf.indentType = PrettyPrinterConfiguration.IndentType.SPACES
               conf.isPrintComments = true
               conf.isPrintJavadoc = true
               editor.codeArea.replaceText(JavaParser().parse(editor.text).result.get().toString(conf))
           }
        }
    }

}