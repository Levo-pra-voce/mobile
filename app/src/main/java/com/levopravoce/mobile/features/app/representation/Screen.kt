package com.levopravoce.mobile.features.app.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    padding: Dp? = null,
    content: @Composable () -> Unit
) {
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = MaterialTheme.customColorsShema.background)
            .then(modifier),
        verticalArrangement,
        horizontalAlignment
    ) {
        Column(Modifier.padding(padding ?:16.dp)) {
            content()
        }
    }
}