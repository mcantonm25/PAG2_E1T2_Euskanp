package com.jaubsa.euskanp.ui.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

// Hau erabiltzen da lehenetsitako hizkuntza hautatzeko aplikazioa lehen aldiz hasten denean
fun updateLocale(context: Context, languageCode: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(android.app.LocaleManager::class.java)?.applicationLocales =
            LocaleList.forLanguageTags(languageCode)
    } else {
        val appLocale = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}