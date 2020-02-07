package dev.itsu.cometide.plugin.loader

import java.io.File
import java.net.URLClassLoader

class PluginClassLoader(val parentLoader: ClassLoader, val file: File) : URLClassLoader(arrayOf(file.toURI().toURL()), parentLoader) {

    private val classes = mutableMapOf<String, Class<*>>()
    private val classLoaders = mutableMapOf<String, PluginClassLoader>()

    override fun findClass(name: String): Class<*> = this.findClass(name, true)

    protected fun findClass(name: String, checkGlobal: Boolean): Class<*> {
        if (name.startsWith("dev.itsu.cometide")) throw ClassNotFoundException(name)

        var result = classes[name]
        if (result == null) {
            if (checkGlobal) result = getClassByName(name)
            if (result == null) {
                result = super.findClass(name)
                if (result != null) setClass(name, result)
            }
            classes[name] = result!!
        }
        return result
    }

    fun getClasses(): Set<String> = classes.keys

    fun getClassByName(name: String): Class<*>? {
        var cachedClass = classes[name]
        if (cachedClass != null) {
            return cachedClass

        } else {
            classLoaders.values.forEach {
                cachedClass = it.findClass(name, false)
                if (cachedClass != null) return cachedClass as Class<*>
            }
        }

        return null
    }

    fun setClass(name: String, clazz: Class<*>) {
        if (!classes.containsKey(name)) classes[name] = clazz
    }

    private fun removeClass(name: String) = classes.remove(name)

    fun put(name: String, loader: PluginClassLoader) {
        classLoaders[name] = loader
    }
}