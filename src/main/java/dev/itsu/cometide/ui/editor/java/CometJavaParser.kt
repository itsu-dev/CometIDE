package dev.itsu.cometide.ui.editor.java

import com.github.javaparser.JavaParser
import dev.itsu.cometide.lang.BaseLang
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.editor.java.visitor.ErrorMarker
import dev.itsu.cometide.ui.editor.java.visitor.GrammerMarker

object CometJavaParser {

    fun parse(text: String, editorImpl: JavaEditorImpl) {
        val unit = JavaParser().parse(text)
        if (unit.result.isPresent) {
            if (unit.problems.size == 0) {
                val result = unit.result.get()
                result.accept(GrammerMarker(editorImpl), "")
                UIManager.getBottomBarController().getDataModel().setStatus(BaseLang.getLang("bottombar.status.ready"))
            } else {
                ErrorMarker.mark(unit.problems, editorImpl)
            }
        }
    }

}