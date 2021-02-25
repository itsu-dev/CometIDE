package dev.itsu.cometide.ui.editor.java

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.Expression
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
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.TwoDimensional
import java.lang.IndexOutOfBoundsException

class JavaEditorImpl(treeItemData: TreeItemData) : AbstractEditor(treeItemData, "java") {

    private val popup = Popup()
    private val label = Label()
    private var consequence: ParseConsequence? = null
    private val keyListener = JavaEditorKeyListener(this)

    companion object {
        lateinit var INDENT: String
        const val INDENT_TEXT = " "
        const val INDENT_COUNT = 4
        const val SMART_INDENT_REMOVAL_ENABLED = true
        const val AUTO_INDENT_ENABLED = true
    }

    init {
        popup.content.addAll(label)
        popup.width = 200.0
        label.style = "-fx-background-color: #0F111A"
        label.isWrapText = true
        codeArea.setOnKeyPressed { keyListener.onKeyPressed(it) }

        INDENT = ""
        for (i in 0 until INDENT_COUNT) {
            INDENT += INDENT_TEXT
        }
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
                    try {
                        this@JavaEditorImpl.codeArea.setStyle(parseArea.paragraph, parseArea.from, parseArea.to, parseArea.style)
                    } catch (e: IndexOutOfBoundsException) {}
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

                //println(text.removePrefix(" > "))
                GlobalScope.launch(Dispatchers.JavaFx) {
                    setTextLabel(text.removePrefix(" > "))
                }
            }
        }
    }

    override fun onMouseOverTextEnd(event: MouseOverTextEvent) {
        popup.hide()
    }

    fun doIndent() {
        if (!SMART_INDENT_REMOVAL_ENABLED) return

        var i = codeArea.currentParagraph

        var text = ""
        while (i >= 0) {
            val previousParagraph = codeArea.paragraphs[i]
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

        val current = codeArea.paragraphs[codeArea.currentParagraph]
        if (current.text.isEmpty()) codeArea.insertText(codeArea.caretPosition, indent)

        if  (text.endsWith("{")) {
            var additional = "\n"
            for (j in 0 until indentCount - INDENT_COUNT) {
                additional += INDENT_TEXT
            }
            additional += "}"
            codeArea.insertText(codeArea.caretPosition, additional)
            codeArea.moveTo(
                    codeArea.currentParagraph - 1,
                    codeArea.getParagraphLength(codeArea.currentParagraph - 1)
            )
        }
    }

    private fun getIndentCount(text: String): Int {
        var count = 0
        text.forEach {
            if (it.toString() == INDENT_TEXT) count++
            else return count
        }
        return count
    }

    fun doSmartIndentRemoval() {
        if (!AUTO_INDENT_ENABLED) return

        val current = codeArea.paragraphs[codeArea.currentParagraph] ?: return
        val texts = codeArea.paragraphs[codeArea.currentParagraph].substring(0, codeArea.caretPosition)
        if (texts.replace(INDENT_TEXT, "").count() == 0 && (texts.count() % INDENT_COUNT != 0)) {
            val indentCount = getIndentCount(current.text)
            codeArea.replaceText(
                    codeArea.currentParagraph,
                    codeArea.caretColumn - indentCount,
                    codeArea.currentParagraph,
                    codeArea.caretColumn,
                    ""
            )
            codeArea.moveTo(codeArea.caretPosition - 1)
            codeArea.deleteNextChar()
        }
    }

}