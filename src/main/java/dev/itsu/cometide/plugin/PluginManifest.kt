package dev.itsu.cometide.plugin

data class PluginManifest(
        val name: String,
        val version: String,
        var targetVersion: String,
        val mainClass: String,
        val shortDescription: String,
        val author: String
) {
    var homepageURL: String? = null
    var description: String? = null
    var terms: String? = null
    var authors: Array<String>? = null
    var loadBefore: String? = null

    var externalMenu: String? = null
    var externalTheme: String? = null
    var externalLanguage: Map<String, String>? = null
}