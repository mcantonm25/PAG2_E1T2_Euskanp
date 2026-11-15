package com.jaubsa.euskanp.ui.utils

import android.content.Context
import org.json.JSONArray
import java.io.File

fun gordeDatuak(
    context: Context,
    oldEmail: String,
    newIzenAbizena: String,
    newTelefonoa: String,
    newEmail: String
): Boolean {
    return try {
        val izenAbizenaSplit = newIzenAbizena.trim().split("",limit=2)
        val newIzena = izenAbizenaSplit.getOrNull(0) ?: ""
        val newAbizena = izenAbizenaSplit.getOrNull(1) ?: ""

        val file = File(context.filesDir, "assets/json/bezeroak.json")
        if (!file.exists()) return false

        val jsonString = file.readText()
        val jsonArray = JSONArray(jsonString)

        for (i in 0..jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            if (jsonObject.optString("emaila") == oldEmail || jsonObject.optString("email") == oldEmail) {
                jsonObject.put("izena", newIzena)
                jsonObject.put("abizena", newAbizena)
                jsonObject.put("telefonoa", newTelefonoa)
                jsonObject.put("emaila", newEmail)
                break
            }
        }

        file.writeText(jsonArray.toString())
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}