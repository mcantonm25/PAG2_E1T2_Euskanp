package com.jaubsa.euskanp.ui.utils

import android.content.Context

class AgendaManager(context: Context) {
    private val prefs = context.getSharedPreferences("euskanp_agenda_prefs", Context.MODE_PRIVATE)

    private fun keyForUser(userEmail: String): String {
        return "saved_events_${userEmail.trim().lowercase()}"
    }

    fun addEventToAgenda(userEmail: String, eventId: String) {
        if (userEmail.isBlank() || eventId.isBlank()) return
        val key = keyForUser(userEmail)
        val currentIds = prefs.getStringSet(key, emptySet())?.toMutableSet() ?: mutableSetOf()
        currentIds.add(eventId)
        prefs.edit().putStringSet(key, currentIds).apply()
    }

    fun removeEventFromAgenda(userEmail: String, eventId: String) {
        if (userEmail.isBlank()) return
        val key = keyForUser(userEmail)
        val currentIds = prefs.getStringSet(key, emptySet())?.toMutableSet() ?: return
        currentIds.remove(eventId)
        prefs.edit().putStringSet(key, currentIds).apply()
    }

    fun getSavedEventsForUser(context: Context, userEmail: String): List<KulturEvent> {
        if (userEmail.isBlank()) return emptyList()
        val savedIds = prefs.getStringSet(keyForUser(userEmail), emptySet()) ?: emptySet()
        if (savedIds.isEmpty()) return emptyList()

        val allEvents = loadKulturEventsFromAssets(context)
        return allEvents.filter { it.id in savedIds }
    }
}