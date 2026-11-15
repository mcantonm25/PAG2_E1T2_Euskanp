package com.jaubsa.euskanp.ui.utils

import android.content.*
import android.util.Xml
import com.jaubsa.euskanp.models.*
import org.xmlpull.v1.XmlPullParser

fun BezeroaEmail(context: Context, emaila: String): Bezeroa? {
    return try {
        context.assets.open("xml/bezeroak.xml").use { inputStream ->
            val parser = Xml.newPullParser()
            parser.setInput(inputStream, "UTF-8")

            var eventType = parser.eventType
            var currentBezeroa: Bezeroa? = null
            var text = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "row") {
                            currentBezeroa = Bezeroa()
                        }
                    }
                    XmlPullParser.TEXT -> {
                        text = parser.text ?: ""
                    }
                    XmlPullParser.END_TAG -> {
                        currentBezeroa?.let { bezeroa ->
                            when (tagName) {
                                "id" -> currentBezeroa = bezeroa.copy(id = text)
                                "izena" -> currentBezeroa = bezeroa.copy(izena = text)
                                "abizena" -> currentBezeroa = bezeroa.copy(abizena = text)
                                "emaila" -> currentBezeroa = bezeroa.copy(emaila = text)
                                "telefonoa" -> currentBezeroa = bezeroa.copy(telefonoa = text)
                                "ostatu_mota" -> currentBezeroa = bezeroa.copy(ostatu_mota = text)
                                "sortzeEguna" -> currentBezeroa = bezeroa.copy(sortzeEguna = text)
                                "row" -> {
                                    if (bezeroa.emaila.trim().equals(emaila.trim(), ignoreCase = true)) {
                                        return bezeroa
                                    }
                                }
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}