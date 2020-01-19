package dev.itsu.cometide.ui.util

import dev.itsu.cometide.model.TreeItemData
import dev.itsu.cometide.plugin.PluginManager
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.io.File

object IconCreator {
    val ICON_DIRECTORY = createImage("img/icon/icon_directory.png")
    val ICON_DIRECTORY_PROJECT = createImage("img/icon/icon_directory_project.png")
    val ICON_DIRECTORY_NEW = createImage("img/icon/icon_directory_new.png")
    val ICON_DIRECTORY_SRC = createImage("img/icon/icon_directory_src.png")
    val ICON_DIRECTORY_RESOURCE = createImage("img/icon/icon_directory_resource.png")
    val ICON_DIRECTORY_PHOTO = createImage("img/icon/icon_directory_photo.png")
    val ICON_DIRECTORY_2 = createImage("img/icon/icon_directory_2.png")
    val ICON_NEXT = createImage("img/icon/icon_next.png")
    val ICON_CODE = createImage("img/icon/icon_code.png")
    val ICON_PHOTO = createImage("img/icon/icon_photo.png")
    val ICON_FILE = createImage("img/icon/icon_file.png")
    val ICON_SETTING = createImage("img/icon/icon_file.png")

    fun createImageFromData(treeItemData: TreeItemData): ImageView {
        val file = File(treeItemData.path)
        var icon = ICON_FILE

        if (treeItemData.root) {
            icon = ICON_DIRECTORY_PROJECT
        } else {
            when (treeItemData.type) {
                TreeItemData.Type.GROUP -> {
                    icon = when (file.name) {
                        "src" -> ICON_DIRECTORY_SRC
                        "resources" -> ICON_DIRECTORY_RESOURCE
                        "img", "images", "image", "icon", "icons" -> ICON_DIRECTORY_PHOTO
                        else -> ICON_DIRECTORY
                    }
                }
                TreeItemData.Type.ITEM -> {
                    icon = when(file.extension.toLowerCase()) {
                        "java", "json", "properties", "md", "xml", "html", "php", "js" -> ICON_CODE
                        "png", "jpg", "jpeg", "gif", "svg" -> ICON_PHOTO
                        else -> ICON_FILE
                    }
                }
            }
        }

        return createImageView(icon)
    }

    fun createImageView(image: Image): ImageView = ImageView(image)

    fun createImage(path: String): Image = createImage(path, IconCreator::class.java.classLoader)

    fun createImage(path: String, clazz: ClassLoader): Image {
        if (path.contains(":")) {
            val split = path.split(":")
            split[0]
            return Image((PluginManager.getInstance().plugins[split[0]]
                    ?: return Image(clazz.getResourceAsStream(path))).classLoader.getResourceAsStream(split[1]))
        } else {
            return Image(clazz.getResourceAsStream(path))
        }
    }
}