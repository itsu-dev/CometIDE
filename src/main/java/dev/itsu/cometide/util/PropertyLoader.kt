package dev.itsu.cometide.util

import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.*

object PropertyLoader {
    fun loadProperties(defaultFile: InputStream, path: String): Properties? {
        val file = File(path)
        if (!file.exists()) {
            try {
                Files.copy(defaultFile, file.toPath())
                defaultFile.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return loadPropertiesFromFile(file)
    }

    fun loadPropertiesFromInputStream(inputStream: InputStream): Properties? {
        try {
            val properties = Properties()
            properties.load(BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)))
            inputStream.close()
            return properties
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun loadPropertiesFromFile(file: File): Properties? {
        try {
            val properties = Properties()
            properties.load(BufferedReader(InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8)))
            return properties
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}