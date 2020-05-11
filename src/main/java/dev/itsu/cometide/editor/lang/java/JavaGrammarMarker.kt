package dev.itsu.cometide.editor.lang.java

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.VariableDeclarationExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import dev.itsu.cometide.editor.IGrammarMarker
import dev.itsu.cometide.editor.model.ParseArea
import dev.itsu.cometide.editor.model.ParseConsequence

class JavaGrammarMarker : VoidVisitorAdapter<String>(), IGrammarMarker {

    private val mConsequence = ParseConsequence()

    override fun getConsequence(): ParseConsequence = mConsequence

    override fun visit(n: VariableDeclarationExpr, arg: String) {
        if (n.begin.isPresent) {
            val commonLength = n.commonType.asString().length
            val begin = n.commonType.begin.get()
            mConsequence.parseAreas.add(ParseArea(begin.line - 1, begin.column - 1, begin.column + commonLength - 1, setOf("variable-type")))
        }
        super.visit(n, arg)
    }

    override fun visit(n: VariableDeclarator, arg: String) {
        if (n.name.begin.isPresent) {
            val commonLength = n.nameAsString.length
            val begin = n.name.begin.get()
            mConsequence.parseAreas.add(ParseArea(begin.line - 1, begin.column - 1, begin.column + commonLength - 1, setOf("variable-name")))
        }
        super.visit(n, arg)
    }

    override fun visit(n: MethodCallExpr, arg: String) {
        if (n.begin.isPresent) {
            val begin = n.name.begin.get()
            val nameLength = n.nameAsString.length
            mConsequence.parseAreas.add(ParseArea(begin.line - 1, begin.column - 1, begin.column + nameLength - 1, setOf("method-call-name")))
        }
        super.visit(n, arg)
    }

    override fun visit(n: FieldDeclaration, arg: String) {
        if (n.begin.isPresent) {
            val typeIndex = n.toString().indexOf(n.commonType.asString())
            val typeLength = n.commonType.asString().length
            val typeBegin = n.begin.get()
            val styleClass = if (n.isStatic) "variable-element-field-static" else "variable-element-field"

            mConsequence.parseAreas.add(ParseArea(typeBegin.line - 1, (typeBegin.column + typeIndex) - 1, (typeBegin.column + typeIndex + typeLength) - 1, setOf("variable-type")))
            n.variables.forEach {
                if (it.begin.isPresent) {
                    val nameBegin = it.name.begin.get()
                    val nameLength = it.nameAsString.length
                    mConsequence.parseAreas.add(ParseArea(nameBegin.line - 1, nameBegin.column - 1, (nameBegin.column + nameLength) - 1, setOf(styleClass)))
                }
            }
        }
        super.visit(n, arg)
    }

    override fun visit(n: MethodDeclaration, arg: String) {
        if (n.name.begin.isPresent) {
            val nameBegin = n.name.begin.get()
            val nameLength = n.nameAsString.length
            mConsequence.parseAreas.add(ParseArea(nameBegin.line - 1, nameBegin.column - 1, nameBegin.column + nameLength - 1, setOf("method-define-name")))
        }

        n.parameters.forEach {
            if (it.type.begin.isPresent) {
                val typeBegin = it.type.begin.get()
                val typeLength = it.type.asString().length
                mConsequence.parseAreas.add(ParseArea(typeBegin.line - 1, typeBegin.column - 1, typeBegin.column + typeLength - 1, setOf("method-define-variable-type")))
            }

            if (it.name.begin.isPresent) {
                val nameBegin = it.name.begin.get()
                val nameLength = it.nameAsString.length
                mConsequence.parseAreas.add(ParseArea(nameBegin.line - 1, nameBegin.column - 1, nameBegin.column + nameLength - 1, setOf("method-define-variable-name")))
            }
        }

        n.annotations.forEach {
            if (it.name.begin.isPresent) {
                val nameBegin = it.name.begin.get()
                val nameLength = it.nameAsString.length
                mConsequence.parseAreas.add(ParseArea(nameBegin.line - 1, nameBegin.column - 2, nameBegin.column + nameLength - 1, setOf("method-define-annotation-name")))
            }
        }

        super.visit(n, arg)
    }

    override fun visit(n: ClassOrInterfaceDeclaration, arg: String) {
        if (n.name.begin.isPresent) {
            val nameBegin = n.name.begin.get()
            val nameLength = n.nameAsString.length
            mConsequence.parseAreas.add(ParseArea(nameBegin.line - 1, nameBegin.column - 1, nameBegin.column + nameLength - 1, setOf("class-define-name")))
        }

        n.extendedTypes.forEach {
            if (n.name.begin.isPresent) {
                val nameBegin = it.name.begin.get()
                val nameLength = it.nameAsString.length
                mConsequence.parseAreas.add(ParseArea(nameBegin.line - 1, nameBegin.column - 1, nameBegin.column + nameLength - 1, setOf("class-define-extended-type")))
            }
        }

        n.implementedTypes.forEach {
            if (n.name.begin.isPresent) {
                val nameBegin = it.name.begin.get()
                val nameLength = it.nameAsString.length
                mConsequence.parseAreas.add(ParseArea(nameBegin.line - 1, nameBegin.column - 1, nameBegin.column + nameLength - 1, setOf("class-define-implemented-type")))
            }
        }
        super.visit(n, arg)
    }
}