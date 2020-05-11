package dev.itsu.cometide.editor.model

import com.github.javaparser.ast.CompilationUnit

class ParseConsequence {

    val parseAreas = mutableListOf<ParseArea>()
    var problems = mutableListOf<ParseProblem>()
    var messages = mutableListOf<String>()
    var unit: CompilationUnit? = null

}