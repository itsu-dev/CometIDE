package dev.itsu.cometide.ui.editor.java

import com.github.javaparser.JavaParser
import dev.itsu.cometide.ui.editor.java.visitor.CometMethodCallVisitor

object CometJavaParser {

    fun parse(text: String, editorImpl: JavaEditorImpl) {
        val unit = JavaParser().parse(text)
        if (unit.result.isPresent) {
            unit.result.get().accept(CometMethodCallVisitor(editorImpl), "ARG!")
        }
    }

}