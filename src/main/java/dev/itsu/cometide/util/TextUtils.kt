package dev.itsu.cometide.util

import dev.itsu.cometide.dao.ProjectDao
import java.io.File

object TextUtils {

    fun shortenProjectPath(path: String): String {
        return "...\\${File(path).toRelativeString(File(ProjectDao.project.rootDir))}"
    }

}