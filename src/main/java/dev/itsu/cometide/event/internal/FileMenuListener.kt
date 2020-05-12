package dev.itsu.cometide.event.internal

import dev.itsu.cometide.dao.ProjectDao
import dev.itsu.cometide.data.EditorData
import dev.itsu.cometide.event.EventListener
import dev.itsu.cometide.event.events.EventHandler
import dev.itsu.cometide.event.events.ui.MenuItemClickedEvent
import javafx.embed.swing.SwingFXUtils
import javafx.scene.SnapshotParameters
import java.io.File
import javax.imageio.ImageIO

class FileMenuListener : EventListener {

    @EventHandler
    fun onMenuClicked(event: MenuItemClickedEvent) {
        when(event.id) {
            "menubar.file.new.directory" -> {
                var file = File(ProjectDao.project.rootDir) //TODO rootDir -> Selecting file
                if (!file.exists()) return
                if (!file.isDirectory) file = file.parentFile
                if (!file.exists()) return

                File("${file.absolutePath}${File.separator}NewDir").mkdir()
            }

            "actionbar.screenshot" -> {
                val image = (EditorData.currentEditor ?: return).codeArea.snapshot(SnapshotParameters(), null)
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", File("C:\\Users\\itsu\\Desktop\\${System.currentTimeMillis()}.png"))
            }
        }
    }

}