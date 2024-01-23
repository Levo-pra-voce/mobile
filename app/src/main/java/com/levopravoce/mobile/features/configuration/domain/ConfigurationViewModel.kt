package com.levopravoce.mobile.features.configuration.domain

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.config.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): ViewModel() {
   fun saveDarkModeState(state: Boolean) {
       preferencesManager.saveDarkModeState(state)
   }

    @Composable
    fun isDarkMode(): Boolean {
         return preferencesManager.isDarkMode()
    }
}