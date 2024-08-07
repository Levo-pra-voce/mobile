package com.levopravoce.mobile.features.app.representation

import android.app.UiModeManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.levopravoce.mobile.ui.theme.MobileTheme
import com.levopravoce.mobile.ui.theme.customColorsShema


@Composable
fun FormInputText(
    value: String,
    modifier: Modifier = Modifier,
    placeHolder: String = "",
    label: String? = null,
    labelModifier: Modifier = Modifier,
    onChange: (String) -> Unit,
    onSubmitted: () -> Any? = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    withBorder: Boolean = true,
    enabled: Boolean = true
) {

    Box {
        if (label != null) {
            Text(
                text = label,
                color = MaterialTheme.customColorsShema.title,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                modifier = Modifier.offset(y = 12.dp).zIndex(1f).then(labelModifier)
            )
        }
        TextField(
            enabled = enabled,
            value = value,
            onValueChange = onChange,
            visualTransformation = visualTransformation,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = {
                    onSubmitted()
                }
            ),
            placeholder = {
                Text(text = placeHolder, color = MaterialTheme.customColorsShema.placeholder)
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.customColorsShema.title,
                unfocusedTextColor = MaterialTheme.customColorsShema.title,
                disabledTextColor = MaterialTheme.customColorsShema.title,
                errorTextColor = MaterialTheme.customColorsShema.title,
                focusedContainerColor = MaterialTheme.customColorsShema.background,
                unfocusedContainerColor = MaterialTheme.customColorsShema.background,
                disabledContainerColor = MaterialTheme.customColorsShema.background,
                errorContainerColor = MaterialTheme.customColorsShema.background,
            ),
            modifier = when (withBorder) {
                true -> modifier
                    .border(1.dp, MaterialTheme.customColorsShema.border)

                false -> modifier
            }
                .background(color = MaterialTheme.customColorsShema.background)
                .padding(top = if (label != null) 12.dp else 0.dp)
                .then(modifier)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UiModeManager.MODE_NIGHT_YES)
@Composable
fun FormInputTextPreview() {
    MobileTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            FormInputText(
                onChange = {},
                value = "",
                placeHolder = "*****",
                label = "Digite seu e-mail:"
            )
        }
    }
}