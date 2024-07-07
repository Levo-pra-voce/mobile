package com.levopravoce.mobile.features.themeCustomization.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levopravoce.mobile.config.PreferencesManager
import com.levopravoce.mobile.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

public fun <T> Flow<T>.mutableStateIn(
    scope: CoroutineScope,
    initialValue: T
): MutableStateFlow<T> {
    val flow = MutableStateFlow(initialValue)

    scope.launch {
        this@mutableStateIn.collect(flow)
    }

    return flow
}

@HiltViewModel
class ThemeCustomizationViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val themeModeFlow = preferencesManager.getUiModeFlow().map {
        val uiMode = preferencesManager.getUiMode()
        return@map uiMode
    }
    private var _themeMode: MutableStateFlow<ThemeMode> =
        themeModeFlow.mutableStateIn(scope = viewModelScope, initialValue = ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode

    init {
        _themeMode.value = preferencesManager.getUiMode()
    }

    fun saveDarkModeState(themeMode: ThemeMode) {
        preferencesManager.saveDarkModeState(themeMode)
        _themeMode.update {
            themeMode
        }
    }
}