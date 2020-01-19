package dev.itsu.cometide.util

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory
import dev.itsu.cometide.data.Settings
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import java.io.OutputStream
import java.lang.Exception
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object XMLLoader {
    fun write(document: Document, file: File) {
        write(document, file.outputStream())
    }

    fun write(document: Document, outputStream: OutputStream) {
        try {
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "4")
            transformer.setOutputProperty(OutputKeys.ENCODING, Settings.getInstance().defaultEncode)
            transformer.transform(DOMSource(document), StreamResult(outputStream))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(file: File): Element?{
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        return document.documentElement
    }
}