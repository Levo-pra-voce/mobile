package com.levopravoce.mobile.features.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@ExperimentalMaterial3Api
class Login {
    @Composable
    fun Login() {
    }

    @Composable
    private fun Title() {
        Text(text = "Login", fontSize = 24.sp)
    }

    @Composable
    fun formInputText(
        label: String,
        placeholder: String,
        onTextChange: (String) -> Unit,
        value: String = ""
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = value,
                onValueChange = onTextChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                ),
                placeholder = {
                    Text(text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.large)
                    .background(color = MaterialTheme.colorScheme.primary)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = MaterialTheme.shapes.large
                    )
                    .fillMaxWidth()
            )
        }
    }

    @Composable
    @Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun formInputTextPreview() {
        Column (
            modifier = Modifier
                .padding(16.dp)
        ) {
            formInputText(
                label = "Email",
                placeholder = "Digite seu email",
                value = "",
                onTextChange = {})
        }
    }
}