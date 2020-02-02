package dev.itsu.cometide.event.internal

import dev.itsu.cometide.data.EditorData
import dev.itsu.cometide.data.ProjectData
import dev.itsu.cometide.event.EventListener
import dev.itsu.cometide.event.events.EventHandler
import dev.itsu.cometide.event.events.ui.MenuItemClickedEvent
import dev.itsu.cometide.ui.UIManager
import javafx.embed.swing.SwingFXUtils
import javafx.scene.SnapshotParameters
import javafx.scene.image.WritableImage
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.ImageWriter

class FileMenuListener : EventListener {

    @EventHandler
    fun onMenuClicked(event: MenuItemClickedEvent) {
        when(event.id) {
            "menubar.file.new.directory" -> {
                var file = File(ProjectData.project.selectingFile)
                if (!file.exists()) return
                if (!file.isDirectory) file = file.parentFile
                if (!file.exists()) return

                File("${file.absolutePath}${File.separator}NewDir").mkdir()
                UIManager.getInstance().splitPane.getProjectTree().reload(ProjectData.project.root)
            }

            "actionbar.screenshot" -> {
                val image = (EditorData.currentEditor ?: return).codeArea.snapshot(SnapshotParameters(), null)
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", File("C:\\Users\\itsu\\Desktop\\${System.currentTimeMillis()}.png"))
            }
        }
    }

}