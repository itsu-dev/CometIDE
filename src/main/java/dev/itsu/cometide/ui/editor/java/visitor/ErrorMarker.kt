package dev.itsu.cometide.ui.editor.java.visitor

import com.github.javaparser.Problem
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.editor.java.JavaEditorImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch

object ErrorMarker {
    var problems: List<Problem> = listOf()

      fun mark(problems: List<Problem>, editorImpl: JavaEditorImpl) {
          this.problems = problems

          GlobalScope.launch(Dispatchers.JavaFx) {
              problems.forEach {
                  if (!it.location.isPresent) return@forEach
                  val range = it.location.get().toRange()
                  if (range.isPresent) {
                      editorImpl.codeArea.setStyle(range.get().begin.line - 1, range.get().begin.column - 1, range.get().begin.column, setOf("problem"))
                  }
                  UIManager.splitPane.getBottomBar().setStatus(it.message)
              }
          }
      }
}