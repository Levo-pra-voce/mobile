package com.levopravoce.mobile.features.app.representation

import android.app.UiModeManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.ui.theme.MobileTheme
import com.levopravoce.mobile.ui.theme.customColorsShema


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputText(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeHolder: String = "",
    label: String? = null,
) {
    Column {
        if (label != null) {
            Text(
                text = label,
                color = MaterialTheme.customColorsShema.title,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
            )
        }
        TextField(
            value = value,
            onValueChange = onChange,
            placeholder = {
                Text(text = placeHolder, color = MaterialTheme.customColorsShema.placeholder)
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.customColorsShema.title,
                containerColor = MaterialTheme.customColorsShema.background,
            ),
            modifier = modifier
                .border(1.dp, MaterialTheme.customColorsShema.border)
                .background(color = MaterialTheme.customColorsShema.background)
            ,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UiModeManager.MODE_NIGHT_YES)
@Composable
fun FormInputTextPreview() {
    MobileTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            FormInputText(onChange = {}, value = "", placeHolder = "*****", label = "Digite seu e-mail:")
        }
    }
}