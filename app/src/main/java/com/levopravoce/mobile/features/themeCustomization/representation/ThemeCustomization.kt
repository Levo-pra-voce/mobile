package com.levopravoce.mobile.features.themeCustomization.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.themeCustomization.domain.ThemeCustomizationViewModel
import com.levopravoce.mobile.ui.theme.ThemeMode
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun ThemeCustomization(
    viewModel: ThemeCustomizationViewModel = hiltSharedViewModel()
) {
    val uiState = viewModel.themeMode.collectAsState();

    val isDarkMode = when (uiState.value) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    Column {
        Header(
            horizontal = Alignment.Start,
        ) {
            Column {
                BackButton(
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Customizar",
                    color = MaterialTheme.customColorsShema.title,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.W200
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = MaterialTheme.customColorsShema.background)
                .padding(top = 24.dp)
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    viewModel.saveDarkModeState(ThemeMode.LIGHT)
                }
            ) {
                RadioButton(
                    selected = !isDarkMode,
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .scale(0.8f),
                    colors = RadioButtonDefaults.colors(
                        unselectedColor = Color.White,
                        selectedColor = Color(0xFF535AFF),
                    )
                )
                Text(text = "Modo Claro", color = MaterialTheme.customColorsShema.title)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    viewModel.saveDarkModeState(ThemeMode.DARK)
                }
            ) {
                RadioButton(
                    selected = isDarkMode,
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .scale(0.8f),
                    colors = RadioButtonDefaults.colors(
                        unselectedColor = Color.White,
                        selectedColor = Color(0xFF535AFF),
                    )
                )
                Text(text = "Modo Escuro", color = MaterialTheme.customColorsShema.title)
            }
        }
    }
}