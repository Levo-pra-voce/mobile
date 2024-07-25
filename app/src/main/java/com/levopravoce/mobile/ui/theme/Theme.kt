package com.levopravoce.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.themeCustomization.domain.ThemeCustomizationViewModel

@Immutable
data class CustomColorsShema(
    val material: ColorScheme,
    val background: Color = Color.Unspecified,
    val title: Color = Color.Unspecified,
    val placeholder: Color = Color.Unspecified,
    val border: Color = Color.Unspecified,
    val button: Color = Color.Unspecified,
    val invertBackground: Color = Color.Unspecified,
    val listTitle: Color = Color.Unspecified,
    val buttonBackground: Color = Color.Unspecified,
    val success: Color = Color.Unspecified,
)

val LightColorSchema = CustomColorsShema(
    material = lightColorScheme(),
    background = White80,
    border = Black80,
    title = Black100,
    placeholder = Black80,
    button = gray80,
    invertBackground = White100,
    listTitle = gray80,
    buttonBackground = gray60,
    success = green100
)


val DarkColorSchema = CustomColorsShema(
    material = darkColorScheme(),
    background = Black80,
    border = White80,
    title = White100,
    placeholder = gray80,
    button = Black100,
    invertBackground = Black100,
    listTitle = gray80,
    buttonBackground = Black100,
    success = green100
)

var LocalColors = compositionLocalOf { LightColorSchema }

val MaterialTheme.customColorsShema: CustomColorsShema
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}


@Composable
fun MobileTheme(
    themeCustomizationViewModel: ThemeCustomizationViewModel = hiltSharedViewModel(),
    content: @Composable () -> Unit,
) {
    val themeMode by themeCustomizationViewModel.themeMode.collectAsState()
    val colors = when (themeMode) {
        ThemeMode.LIGHT -> LightColorSchema
        ThemeMode.DARK -> DarkColorSchema
        ThemeMode.SYSTEM -> if (isSystemInDarkTheme()) DarkColorSchema else LightColorSchema
    }

    CompositionLocalProvider(
        LocalColors provides colors
    ) {
        MaterialTheme(
            content = content,
            colorScheme = colors.material,
        )
    }
}