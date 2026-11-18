package com.jaubsa.euskanp.ui.utils

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

// KulturEvent data class-a JSONetik datuak lortzeko (etiketetik)
data class KulturEvent(
    val id: String,
    val documentName: String,
    val documentDescription: String,
    val eventType: String,
    val eventStartDate: String,
    val eventEndDate: String,
    val formattedShortDate: String,
    val eventTownName: String,
    val eventWhere: String,
    val eventTimeTable: String,
    val eventPrice: String,
    val eventLanguages: String,
    val address: String,
    val company: String,
    val imageUrl: String
)

// Regex behin bakarrik konpilatzen ditugu (errendimendua asko hobetzeko)
private val HTML_TAG_REGEX = Regex("<[^>]*>")
private val WHITESPACE_REGEX = Regex("\\s+")

// Memoriako cachea, JSON fitxategia behin bakarrik irakurtzeko
private var cachedEvents: List<KulturEvent>? = null

fun loadKulturEventsFromAssets(
    context: Context,
    fileName: String = "json/kulturklik.json"
): List<KulturEvent> {
    cachedEvents?.let { return it }

    val events = mutableListOf<KulturEvent>()
    try {
        val rawText = context.assets.open(fileName)
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }
            .trim()
            .removePrefix("\uFEFF")

        if (rawText.startsWith("[")) {
            val jsonArray = JSONArray(rawText)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.optJSONObject(i) ?: continue
                events.add(parseKulturObject(obj, i))
            }
        } else {
            rawText.lines().forEachIndexed { index, line ->
                val cleanLine = line.trim().removeSuffix(",")
                if (cleanLine.startsWith("{") && cleanLine.endsWith("}")) {
                    events.add(parseKulturObject(JSONObject(cleanLine), index))
                }
            }
        }
        cachedEvents = events
        Log.d("Kulturklik", "$fileName fitxategitik, ${events.size} deialdi kargatu dira.")
    } catch (e: Exception) {
        Log.e("Kulturklik", "Error irakurtzean $fileName fitxategia: ${e.message}", e)
    }
    return events
}

fun extractUniqueEventTypes(events: List<KulturEvent>): List<String> {
    return events
        .map { it.eventType.trim() }
        .filter { it.isNotBlank() }
        .distinctBy { it.lowercase() }
        .sorted()
}

private fun parseKulturObject(obj: JSONObject, fallbackIndex: Int): KulturEvent {
    val idObj = obj.optJSONObject("_id")
    val id = idObj?.optString("\$oid")
        ?: obj.optString("_id").ifBlank { fallbackIndex.toString() }

    val startDateObj = obj.optJSONObject("eventStartDate")
    val rawStartDate = startDateObj?.optString("\$date")
        ?: obj.optString("eventStartDate", "")
    val isoStartDate = if (rawStartDate.length >= 10) rawStartDate.substring(0, 10) else rawStartDate

    val endDateObj = obj.optJSONObject("eventEndDate")
    val rawEndDate = endDateObj?.optString("\$date")
        ?: obj.optString("eventEndDate", "")
    val isoEndDate = if (rawEndDate.length >= 10) rawEndDate.substring(0, 10) else rawEndDate

    val shortDate = if (isoStartDate.length >= 10 && isoStartDate[4] == '-' && isoStartDate[7] == '-') {
        "${isoStartDate.substring(8, 10)}/${isoStartDate.substring(5, 7)}/${isoStartDate.substring(2, 4)}"
    } else {
        isoStartDate
    }

    val rawDesc = obj.optString("documentDescription")
        .ifBlank { obj.optString("description") }
        .ifBlank { obj.optString("eventDescription") }
    val cleanDesc = rawDesc
        .replace(HTML_TAG_REGEX, " ")
        .replace("&nbsp;", " ")
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .replace(WHITESPACE_REGEX, " ")
        .trim()

    val imageUrl = obj.optString("eventImage")
        .ifBlank { obj.optString("image") }
        .ifBlank {
            obj.optJSONArray("images")?.optString(0) ?: ""
        }

    return KulturEvent(
        id = id,
        documentName = obj.optString("documentName", "").replace("\"", "").trim(),
        documentDescription = cleanDesc,
        eventType = obj.optString("eventType", "Bestelakoak").trim(),
        eventStartDate = isoStartDate,
        eventEndDate = isoEndDate,
        formattedShortDate = shortDate,
        eventTownName = obj.optString("eventTownName", "").trim(),
        eventWhere = obj.optString("eventWhere", "").trim(),
        eventTimeTable = obj.optString("eventTimeTable", "").trim(),
        eventPrice = obj.optString("eventPrice", "").replace("?", "€").trim(),
        eventLanguages = obj.optString("eventLanguages", "").trim(),
        address = obj.optString("address", "").trim(),
        company = obj.optString("company", "").trim(),
        imageUrl = imageUrl.trim()
    )
}