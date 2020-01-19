package dev.itsu.cometide.util

import dev.itsu.cometide.data.RuntimeData
import java.io.File

object TextUtils {

    fun shortenProjectPath(path: String): String {
        return "...\\${File(path).toRelativeString(File(RuntimeData.getInstance().projectRoot!!))}"
    }

}