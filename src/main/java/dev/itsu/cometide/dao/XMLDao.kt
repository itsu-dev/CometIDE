package dev.itsu.cometide.dao

import dev.itsu.cometide.util.IOUtils
import java.io.File
import java.nio.charset.StandardCharsets

abstract class XMLDao : Dao {

    protected fun store(string: String) {
        File(getFilePath()).bufferedWriter(StandardCharsets.UTF_8).use {
            it.write(string)
            it.flush()
        }
    }

    fun readFromInternal(): String {
        return IOUtils.readFileFromInputStream(XMLDao::class.java.classLoader.getResourceAsStream(getFilePath().substring(1)))
    }

    abstract fun getFilePath(): String
}