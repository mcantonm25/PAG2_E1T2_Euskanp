package com.jaubsa.euskanp.ui.utils

import android.content.Context
import androidx.core.content.edit

// Klase hau erabiltzen da baieztazeko ea sesioa hasita daukan ala ez. Saioa hasita badauka, saioa hasita mantentzen da memoria lokalean.
class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("euskanp_session", Context.MODE_PRIVATE) // Context.MODE_PRIVATE esanez, sesioa soilik lokalean gordetzen da.

    // Metodoa saioa gordetzeko lokalean
    fun saveUserSession(email: String) {
        prefs.edit { putString("logged_user_email", email) }
    }

    // Metodoa saioa zein den lortzeko
    fun getUserSession(): String? {
        return prefs.getString("logged_user_email", null)
    }

    // Metodoa saioa ezabatzeko
    fun clearSession() {
        prefs.edit { remove("logged_user_email") }
    }
}