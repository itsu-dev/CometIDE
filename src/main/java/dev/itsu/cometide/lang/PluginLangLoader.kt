package dev.itsu.cometide.lang

import dev.itsu.cometide.data.EnvironmentSettings
import dev.itsu.cometide.data.Settings
import dev.itsu.cometide.plugin.loader.PluginClassLoader
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*
import java.util.jar.JarFile
import kotlin.streams.toList

object PluginLangLoader {

    fun load(languages: Map<String, String>, file: File) {
        if (file.extension != "jar") return

        languages.forEach { (language, path) ->
            if (BaseLang.lang == language) {
                val properties = Properties()
                val jar = JarFile(file)
                val entry = jar
                        .stream()
                        .filter { it.name == path }
                        .toList()
                        .first()
                properties.load(BufferedReader(InputStreamReader(jar.getInputStream(entry)
                        ?: return@forEach, Settings.getInstance().defaultEncode)))
                properties.forEach { (key, value) ->
                    BaseLang.strings[key] = value
                }
            }
        }
    }
}