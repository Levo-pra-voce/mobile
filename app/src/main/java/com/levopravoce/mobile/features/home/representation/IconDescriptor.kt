package com.levopravoce.mobile.features.home.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun IconDescriptor(
    id: Int,
    contentDescription: String,
    title: String,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row {
            Image(
                painter = painterResource(id),
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.border),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.padding(start = 24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = MaterialTheme.customColorsShema.title,
            fontSize = 14.sp,
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}