package com.levopravoce.mobile.config

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

class PreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    fun saveDarkModeState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("darkMode", state)
        editor.apply()
    }

    @Composable
    fun isDarkMode(): Boolean {
        if (sharedPreferences.contains("darkMode")) {
            return sharedPreferences.getBoolean("darkMode", false)
        }
        return isSystemInDarkTheme();
    }
}