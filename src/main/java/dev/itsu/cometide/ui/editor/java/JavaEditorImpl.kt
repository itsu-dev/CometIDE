package dev.itsu.cometide.ui.editor.java

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.expr.LambdaExpr
import com.github.javaparser.ast.nodeTypes.NodeWithBody
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName
import com.github.javaparser.ast.stmt.*
import dev.itsu.cometide.editor.lang.java.JavaKeywordMarker
import dev.itsu.cometide.editor.lang.java.JavaProgramParser
import dev.itsu.cometide.editor.model.ParseConsequence
import dev.itsu.cometide.lang.BaseLang
import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.ui.UIManager
import dev.itsu.cometide.ui.editor.AbstractEditor
import javafx.scene.control.Label
import javafx.stage.Popup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.fxmisc.richtext.StyleClassedTextArea
import org.fxmisc.richtext.event.MouseOverTextEvent
import org.fxmisc.richtext.model.StyleSpan
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import org.fxmisc.richtext.model.TwoDimensional
import java.util.regex.Pattern

class JavaEditorImpl(treeItemData: TreeItemData) : AbstractEditor(treeItemData, "java") {

    private val popup = Popup()
    private val label = Label()
    private var consequence: ParseConsequence? = null
    private val keyListener = KeyListener(this)

    init {
        popup.content.addAll(label)
        popup.width = 200.0
        label.style = "-fx-background-color: #0F111A"
        label.isWrapText = true
        codeArea.setOnKeyPressed { keyListener.onKeyPressed(it) }
    }

    override fun setUp(codeArea: StyleClassedTextArea) {

    }

    override fun onAppliedHighlighting() {
        UIManager.getBottomBarController().getDataModel().setStatus("Highlighting...")

        GlobalScope.launch(Dispatchers.JavaFx) {
            GlobalScope.async(Dispatchers.Default) {
                JavaProgramParser.parse(this@JavaEditorImpl.codeArea.text)

            }.await().let {
                consequence = it
                it.parseAreas.forEach { parseArea ->
                    this@JavaEditorImpl.codeArea.setStyle(parseArea.paragraph, parseArea.from, parseArea.to, parseArea.style)
                }

                UIManager.getBottomBarController().getDataModel().setStatus(BaseLang.getLang("bottombar.status.ready"))
            }
        }

    }

    override fun computeHighlighting(text: String): StyleSpans<Collection<String>> {
        return JavaKeywordMarker.mark(text)
    }

    override fun onMouseOverTextStart(event: MouseOverTextEvent) {
        (consequence ?: return).problems.stream()
                .filter {
                    val mousePosition = codeArea.offsetToPosition(event.characterIndex, TwoDimensional.Bias.Forward)
                    return@filter it.paragraph == mousePosition.major && it.from == mousePosition.minor
                }
                .forEach {
                    label.text = it.message
                    popup.show(codeArea, event.screenPosition.x, event.screenPosition.y + 10)
                }
    }

    override fun onCaretPositionChanged(paragraph: Int, column: Int) {
        consequence?.unit ?: return

        GlobalScope.launch(Dispatchers.Default) {
            var currentBlock: Statement? = null
            consequence!!.unit!!.findAll(Statement::class.java).forEach {
                if (it.range.isPresent) {
                    if (paragraph + 1 in it.begin.get().line until it.end.get().line) {
                        currentBlock = it
                        return@forEach
                    }
                }
            }

            currentBlock ?: return@launch

            var text = ""

            fun getBlockName(node: Node): String? {
                if (node is Statement) {
                    return when {
                        node.isDoStmt -> "do {...}"
                        node.isForStmt -> "for {...}"
                        node.isTryStmt -> "try {...}"
                        node.isWhileStmt -> "while {...}"
                        node.isIfStmt -> "if {...}"
                        node.isForEachStmt -> "forEach {...}"
                        node.isSwitchStmt -> "switch {...}"
                        node.isEmptyStmt -> "{...}"
                        else -> null
                    }

                } else if (node is Expression) {
                    return when {
                        node.isObjectCreationExpr -> "(anonymous())"
                        node.isLambdaExpr -> "lambda {...}"
                        else -> null
                    }

                } else {
                    return when (node) {
                        is CatchClause -> "catch {...}"
                        is SwitchEntry -> "case {...}"
                        else -> "unknown (${node.javaClass.simpleName})"
                    }
                }
            }

            fun process(node: Node) {
                if (node.parentNode.isPresent) {
                    if (getBlockName(node) != null) {
                        text = " > " + (if (node is NodeWithSimpleName<*>) ((node as NodeWithSimpleName<*>).nameAsString +
                                if (node is MethodDeclaration) "()" else "") else getBlockName(node)) + text
                    }
                    process(node.parentNode.get())
                }
            }

            if (currentBlock != null) {
                process(currentBlock as Node)
                println(text.removePrefix(" > "))

                GlobalScope.launch(Dispatchers.JavaFx) {
                    setTextLabel(text.removePrefix(" > "))
                }
            }
        }
    }

    override fun onMouseOverTextEnd(event: MouseOverTextEvent) {
        popup.hide()
    }
}