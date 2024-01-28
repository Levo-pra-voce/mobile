package com.levopravoce.mobile.config

import android.content.Context
import com.levopravoce.mobile.ui.theme.ThemeMode

class PreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    fun saveDarkModeState(themeMode: ThemeMode) {
        val editor = sharedPreferences.edit()
        editor.putString("darkMode", themeMode.name)
        editor.apply()
    }

    fun getUiMode(): ThemeMode {
        return when (sharedPreferences.getString("darkMode", ThemeMode.SYSTEM.name)) {
            ThemeMode.DARK.name -> ThemeMode.DARK
            ThemeMode.LIGHT.name -> ThemeMode.LIGHT
            else -> ThemeMode.SYSTEM
        }
    }
}