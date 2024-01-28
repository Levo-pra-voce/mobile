package com.levopravoce.mobile.features.home.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.R
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.auth.domain.AuthViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun UserHeader(
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val uiState = authViewModel.uiState.collectAsState();

    Header {
        Image(
            painter = painterResource(R.drawable.person_icon),
            contentDescription = "icone da pessoa",
            contentScale = ContentScale.FillHeight,
        )
        Text(
            text = "Olá, ${uiState.value.data?.firstName ?: "Nome não encontrado"}",
            color = MaterialTheme.customColorsShema.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}