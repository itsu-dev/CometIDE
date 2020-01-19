package dev.itsu.cometide.ui.util

import dev.itsu.cometide.plugin.loader.PluginClassLoader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.util.jar.JarEntry
import java.util.jar.JarFile

object ThemeInstaller {

    fun installTheme(file: File, themeRoot: String, classLoader: ClassLoader, destination: String): Boolean {

        val split = themeRoot.split("/")
        if ((File(destination).list() ?:return false).contains(split[split.size - 1])) {
            println("Theme already exists!")
            return false
        }

        when (file.extension) {
            "jar" -> {
                val split = file.path.split(":")
                val path = split[split.size - 1].split("!")[0]
                val targetFile = File(path)

                try {
                    val jarFile = JarFile(targetFile)
                    val entries = jarFile.entries()
                    var entry: JarEntry
                    while (entries.hasMoreElements()) {
                        entry = entries.nextElement()
                        if (entry.name.startsWith(themeRoot)) {
                            if (!process(entry, classLoader, themeRoot, destination)) return false
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return true
    }

    private fun process(entry: JarEntry, classLoader: ClassLoader, themeRoot: String, destination: String): Boolean {
        val themeRoot = entry.name.replaceFirst(themeRoot, "")
        if (File("$destination/$themeRoot").exists()) return false
        if (entry.isDirectory) {
            val dir = File("$destination/$themeRoot")
            if (!dir.exists()) dir.mkdirs()
        } else {
            try {
                Files.copy(classLoader.getResourceAsStream(entry.name)
                        ?: return false, File("$destination/$themeRoot").toPath())
            } catch (e: IOException) {
                return false
            }
        }
        return true
    }
}