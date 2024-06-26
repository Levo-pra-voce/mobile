package com.levopravoce.mobile.features.app.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.levopravoce.mobile.R
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {

    val navController = navControllerContext.current

    Image(
        painter = painterResource(id = R.drawable.baseline_arrow_back_16),
        contentDescription = "Voltar",
        colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.placeholder),
        contentScale = ContentScale.FillHeight,
        modifier = modifier
            .clickable {
                if (enabled) {
                    if (onClick == null) navController?.navigateUp() else onClick()
                }
            }
    )
}