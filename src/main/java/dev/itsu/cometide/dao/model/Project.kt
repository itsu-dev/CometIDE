package dev.itsu.cometide.dao.model

import javax.xml.bind.annotation.*

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
class Project(
        @field:XmlElement(name = "timeStamp") var timeStamp: Long,
        @field:XmlElement(name = "rootDir") var rootDir: String,
        @field:XmlElement(name = "projectName") var projectName: String,
        @field:XmlElement(name = "previousSession") var previousSession: PreviousSession,
        @field:XmlElement(name = "javaProject") var javaProject: JavaProject
) {
    constructor() : this(-1, "", "", PreviousSession(), JavaProject())
}

@XmlAccessorType(XmlAccessType.FIELD)
class Tab(
        @field:XmlElement(name = "filePath") var filePath: String,
        @field:XmlElement(name = "line") var line: Int,
        @field:XmlElement(name = "column") var column: Int
) {
    constructor() : this("", -1, -1)
}

@XmlAccessorType(XmlAccessType.FIELD)
class PreviousSession(
        @field:XmlElement(name = "tabIndex") var tabIndex: Int,
        @field:XmlElementWrapper(name = "tabs") @field:XmlElement(name = "tab") var tabs: MutableList<Tab>
) {
    constructor() : this(-1, mutableListOf())
}

@XmlAccessorType(XmlAccessType.FIELD)
class JavaProject(
        @field:XmlElement(name = "sourceDir") var sourceDir: String
) {
    constructor() : this("")
}