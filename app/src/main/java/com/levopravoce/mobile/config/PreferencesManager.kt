package com.levopravoce.mobile.config

import android.content.Context
import android.content.SharedPreferences
import com.levopravoce.mobile.ui.theme.ThemeMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow

fun SharedPreferences.getFlowForKey(key: String) = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, callbackKey ->
        if (key == callbackKey) {
            trySend(callbackKey)
        }
    }
    registerOnSharedPreferenceChangeListener(listener)
    if (contains(key)) {
        send(key)
    }
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}.buffer(Channel.UNLIMITED) // so trySend never fails

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

    fun getUiModeFlow() = sharedPreferences.getFlowForKey("darkMode")

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}