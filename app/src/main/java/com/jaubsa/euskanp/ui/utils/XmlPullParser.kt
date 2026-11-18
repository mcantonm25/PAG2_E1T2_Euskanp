package com.jaubsa.euskanp.ui.utils

import android.content.Context
import android.util.Log
import android.util.Xml
import com.jaubsa.euskanp.models.Bezeroa
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

// Fitxategien bideak eta izenak definitzen ditugu
private const val ASSET_XML_PATH = "xml/bezeroak.xml" // Hasierako XMLa (assets karpetan)
private const val INTERNAL_XML_NAME = "bezeroak.xml"    // Barne-memorian gordeko den kopia editagarria

// bezeroak.xml fitxategiaren etiketa (tag) izenak
private const val NODE_BEZEROA = "row"
private const val TAG_ID = "id"
private const val TAG_IZENA = "izena"
private const val TAG_ABIZENA = "abizena"
private const val TAG_EMAILA = "emaila"
private const val TAG_TELEFONOA = "telefonoa"
private const val TAG_OSTATU_MOTA = "ostatu_mota"
private const val TAG_SORTZE_EGUNA = "sortzeEguna"

// Assets karpetako XML fitxategia barne-memoriara kopiatzen duen funtzio laguntzailea
private fun copyAssetToInternal(context: Context, internalFile: File) {
    context.assets.open(ASSET_XML_PATH).use { input ->
        internalFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
}

// Editatu daitekeen barne-memoriako XML fitxategia eskuratzen du. Ez bada existitzen edo hutsik badago, assets-etik kopiatzen du lehen aldiz.
private fun getEditableXmlFile(context: Context): File {
    val internalFile = File(context.filesDir, INTERNAL_XML_NAME)
    if (!internalFile.exists() || internalFile.length() == 0L) {
        copyAssetToInternal(context, internalFile)
    }
    return internalFile
}

// XML fitxategian bezero bat bilatzen du bere emailaren bidez (Login eta Profila pantailetarako)
fun BezeroaEmail(context: Context, emaila: String): Bezeroa? {
    val file = getEditableXmlFile(context)
    val doc = try {
        // Fitxategia irakurtzen eta DOM zuhaitz bihurtzen saiatzen da
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
    } catch (e: Exception) {
        // Gordetzean XML fitxategia hondatu bada, assets-etik berrezartzen du automatikoki
        Log.e("XmlPullParser", "XML korruptua detektatu da, assets-etik berrezartzen: ${e.printStackTrace()}")
        copyAssetToInternal(context, file)
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
    }

    return try {
        doc.documentElement.normalize()
        val bezeroakList = doc.getElementsByTagName(NODE_BEZEROA)
        val targetEmail = emaila.trim() // Hasierako eta amaierako hutsuneak kentzen ditu

        // <bezeroa> nodo guztiak banan-banan zeharkatzen ditu
        for (i in 0 until bezeroakList.length) {
            val node = bezeroakList.item(i)
            if (node is Element) {
                val currentEmail = node.getTagText(TAG_EMAILA)
                    .ifEmpty { node.getTagText("email") }
                    .trim()

                // Emaila bat datorren egiaztatzen du (letra larriak eta xeheak bereizi gabe)
                if (currentEmail.equals(targetEmail, ignoreCase = true)) {
                    return Bezeroa(
                        id = node.getTagText(TAG_ID),
                        izena = node.getTagText(TAG_IZENA),
                        abizena = node.getTagText(TAG_ABIZENA),
                        emaila = currentEmail,
                        telefonoa = node.getTagText(TAG_TELEFONOA),
                        ostatu_mota = node.getTagText(TAG_OSTATU_MOTA),
                        sortzeEguna = node.getTagText(TAG_SORTZE_EGUNA)
                    )
                }
            }
        }
        null
    } catch (e: Exception) {
        Log.e("XmlPullParser", "Errorea XML fitxategia irakurtzean: ${e.printStackTrace()}")
        null
    }
}

// Datuak.kt pantailan aldatutako datuak XML fitxategian eguneratzen eta gordetzen ditu
fun gordeDatuak(
    context: Context,
    oldEmail: String,
    newIzenAbizena: String,
    newTelefonoa: String,
    newEmail: String
): Boolean {
    return try {
        // Izen-abizenak espazioaren arabera banatzen ditu (izena eta abizena bereizteko)
        val izenAbizenaSplit = newIzenAbizena.trim().split("\\s+".toRegex(), limit = 2)
        val newIzena = izenAbizenaSplit.getOrNull(0) ?: ""
        val newAbizena = izenAbizenaSplit.getOrNull(1) ?: ""

        val file = getEditableXmlFile(context)
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        doc.documentElement.normalize()

        val bezeroakList = doc.getElementsByTagName(NODE_BEZEROA)
        val targetOldEmail = oldEmail.trim()
        val bezeroak = mutableListOf<Bezeroa>()
        var aurkitua = false

        // Bezero guztiak zerrenda batean kargatzen ditu, eta bilatutako bezeroaren datuak eguneratzen ditu
        for (i in 0 until bezeroakList.length) {
            val node = bezeroakList.item(i)
            if (node is Element) {
                val currentEmail = node.getTagText(TAG_EMAILA)
                    .ifEmpty { node.getTagText("email") }
                    .trim()

                if (currentEmail.equals(targetOldEmail, ignoreCase = true)) {
                    // Bezeroa aurkitu da: datu berriekin gehitzen dugu zerrendara
                    aurkitua = true
                    bezeroak.add(
                        Bezeroa(
                            id = node.getTagText(TAG_ID),
                            izena = newIzena,
                            abizena = newAbizena,
                            emaila = newEmail.trim(),
                            telefonoa = newTelefonoa.trim(),
                            ostatu_mota = node.getTagText(TAG_OSTATU_MOTA),
                            sortzeEguna = node.getTagText(TAG_SORTZE_EGUNA)
                        )
                    )
                } else {
                    // Beste bezeroak bere horretan mantentzen dira
                    bezeroak.add(
                        Bezeroa(
                            id = node.getTagText(TAG_ID),
                            izena = node.getTagText(TAG_IZENA),
                            abizena = node.getTagText(TAG_ABIZENA),
                            emaila = currentEmail,
                            telefonoa = node.getTagText(TAG_TELEFONOA),
                            ostatu_mota = node.getTagText(TAG_OSTATU_MOTA),
                            sortzeEguna = node.getTagText(TAG_SORTZE_EGUNA)
                        )
                    )
                }
            }
        }

        // Bezeroa ez bada aurkitu, false itzultzen du ("Errorea gordean" erakusteko)
        if (!aurkitua) return false

        // Fitxategi osoa berriro idazten du Androiden XmlSerializer natiboa erabiliz (fitxategia ez hondatzeko)
        val rootTag = doc.documentElement.tagName.ifBlank { "rows" }
        file.outputStream().use { out ->
            val serializer = Xml.newSerializer()
            serializer.setOutput(out, "UTF-8")
            serializer.startDocument("UTF-8", true)
            serializer.startTag("", rootTag)

            bezeroak.forEachIndexed { index, b ->
                serializer.startTag("", NODE_BEZEROA)
                serializer.attribute("", "num", (index + 1).toString())
                serializer.writeTag(TAG_ID, b.id)
                serializer.writeTag(TAG_IZENA, b.izena)
                serializer.writeTag(TAG_ABIZENA, b.abizena)
                serializer.writeTag(TAG_EMAILA, b.emaila)
                serializer.writeTag(TAG_TELEFONOA, b.telefonoa)
                serializer.writeTag(TAG_OSTATU_MOTA, b.ostatu_mota)
                serializer.writeTag(TAG_SORTZE_EGUNA, b.sortzeEguna)
                serializer.endTag("", NODE_BEZEROA)
            }

            serializer.endTag("", rootTag)
            serializer.endDocument()
            serializer.flush()
        }

        true
    } catch (e: Exception) {
        Log.e("XmlPullParser", "Errorea XML fitxategia gordetzean: ${e.printStackTrace()}")
        false
    }
}

// XmlSerializer-en etiketa bat ireki, testua idatzi eta ixteko funtzio laguntzailea
private fun org.xmlpull.v1.XmlSerializer.writeTag(tag: String, value: String) {
    startTag("", tag)
    text(value)
    endTag("", tag)
}

// Elementu baten barruko etiketa jakin baten testua irakurtzeko funtzio laguntzailea
private fun Element.getTagText(tagName: String): String {
    return getElementsByTagName(tagName).item(0)?.textContent?.trim() ?: ""
}