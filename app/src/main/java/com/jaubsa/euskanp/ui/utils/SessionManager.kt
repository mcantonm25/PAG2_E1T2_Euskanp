package com.jaubsa.euskanp.ui.utils

import android.content.Context
import androidx.core.content.edit

// Klase hau erabiltzen da baieztazeko ea sesioa hasita daukan ala ez. Saioa hasita badauka, saioa hasita mantentzen da memoria lokalean.
class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("euskanp_session", Context.MODE_PRIVATE)

    fun saveUserSession(email: String) {
        prefs.edit { putString("logged_user_email", email) }
    }

    fun getUserSession(): String? {
        return prefs.getString("logged_user_email", null)
    }

    fun clearSession() {
        prefs.edit { remove("logged_user_email") }
    }
}