package com.levopravoce.mobile.features.app.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Header(
    horizontal: Alignment.Horizontal = Alignment.CenterHorizontally,
    vertical: Arrangement.Vertical = Arrangement.Center,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.customColorsShema.invertBackground)
            .heightIn(min = 148.dp, max = 148.dp)
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = horizontal,
        verticalArrangement = vertical
    ) {
        content()
    }
}