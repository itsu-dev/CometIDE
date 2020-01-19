package dev.itsu.cometide.lang

import dev.itsu.cometide.data.EnvironmentSettings
import dev.itsu.cometide.util.PropertyLoader
import java.util.*

object BaseLang {

    lateinit var strings: Properties

    lateinit var lang: String
    lateinit var fallback: String

    fun init(lang: String, fallback: String) {
        this.lang = lang
        this.fallback = fallback

        val inputStream = BaseLang::class.java.classLoader.getResourceAsStream("lang/$lang.properties")
                ?: BaseLang::class.java.classLoader.getResourceAsStream("lang/$fallback.properties")

        strings = PropertyLoader.loadPropertiesFromInputStream(inputStream) ?: throw NullPointerException("Lang Error!")
    }

    fun getLang(key: String, vararg args: Any): String {
        var value = strings.getProperty(key) ?: getLangFromFallback(key) ?: return "null"
        args.forEachIndexed { index, arg -> value = value.replace("\\{%$index\\\\}", arg.toString()) }
        return value
    }

    fun getLangFromFallback(key: String): String? {
        val properties = PropertyLoader.loadPropertiesFromInputStream(BaseLang::class.java.classLoader.getResourceAsStream("lang/" + EnvironmentSettings.FALLBACK_LANGUAGE + ".properties") ?: return "")
                ?: return null
        return properties.getProperty(key)
    }

}