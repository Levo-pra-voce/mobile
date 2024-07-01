package com.levopravoce.mobile.features.user.representation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.config.termsLink
import com.levopravoce.mobile.features.order.representation.newOrder.RoundedCheckbox
import com.levopravoce.mobile.features.start.representation.bottomBorder
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Terms(
    onChange: (value: Boolean) -> Unit,
) {
    val context = LocalContext.current
    var value by remember { mutableStateOf(false) }
    Row(
        Modifier
            .padding(top = 24.dp, bottom = 8.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .bottomBorder(1.dp, MaterialTheme.customColorsShema.placeholder)
                .clickable {
                    val browser = Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
                    context.startActivity(browser)
                }
            ,
            text = "Termos de uso:",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.customColorsShema.placeholder
        )
        RoundedCheckbox(
            modifier = Modifier.padding(start = 8.dp, top = 10.dp),
            checked = value,
            onCheckedChange = {
                value = it
                onChange(it)
            }
        )
    }
}