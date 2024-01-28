package com.levopravoce.mobile.features.themeCustomization.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.config.PreferencesManager
import com.levopravoce.mobile.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class ThemeCustomizationUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)

@HiltViewModel
class ThemeCustomizationViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): ViewModel() {

    private val _uiState = MutableStateFlow(ThemeCustomizationUiState())

    val uiState: StateFlow<ThemeCustomizationUiState> = _uiState

    init {
        _uiState.value = ThemeCustomizationUiState(themeMode = preferencesManager.getUiMode())
    }

   fun saveDarkModeState(themeMode: ThemeMode) {
        preferencesManager.saveDarkModeState(themeMode)
        _uiState.value = ThemeCustomizationUiState(themeMode = themeMode)
   }
}