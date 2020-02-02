package dev.itsu.cometide.util

import dev.itsu.cometide.data.ProjectData
import java.io.File

object TextUtils {

    fun shortenProjectPath(path: String): String {
        return "...\\${File(path).toRelativeString(File(ProjectData.project.root))}"
    }

}