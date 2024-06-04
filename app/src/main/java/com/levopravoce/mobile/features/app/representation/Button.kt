package com.levopravoce.mobile.features.app.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    disabled: Boolean = false,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(MaterialTheme.customColorsShema.invertBackground)
            .clickable {
                if (!disabled) {
                    onClick()
                }
            }.then(modifier),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.customColorsShema.title
            )
        }
    }
}