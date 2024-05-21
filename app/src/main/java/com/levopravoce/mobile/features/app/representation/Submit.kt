package com.levopravoce.mobile.features.app.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Submit(
    onSubmit: () -> Unit,
    isLoading: Boolean,
    title: String,
    modifier: Modifier = Modifier
) {

    Column(modifier) {
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .then(modifier),
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.customColorsShema.button,
            ),
            enabled = !isLoading
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                if (!isLoading) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color = MaterialTheme.customColorsShema.title
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Loading(
                            size = 12.dp,
                        )
                    }
                }
            }
        }
    }
}