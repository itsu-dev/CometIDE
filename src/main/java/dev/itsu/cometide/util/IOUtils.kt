package dev.itsu.cometide.util

import java.io.File
import java.io.InputStream
import java.nio.charset.StandardCharsets

object IOUtils {

    fun getProjectInputStream(path: String): InputStream? = IOUtils::class.java.classLoader.getResourceAsStream(path)

    fun readFileFromProject(path: String): String = readFileFromInputStream(getProjectInputStream(path)!!)

    fun readFileFromFile(file: File): String = readFileFromInputStream(file.inputStream())

    fun readFileFromInputStream(inputStream: InputStream): String = inputStream.use {
        it.bufferedReader(StandardCharsets.UTF_8).readText()
    }

}