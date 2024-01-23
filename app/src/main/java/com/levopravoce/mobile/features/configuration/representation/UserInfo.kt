package com.levopravoce.mobile.features.configuration.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.features.auth.domain.AuthViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun UserInfo(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.customColorsShema.background)
    ) {
        Row(
            Modifier.padding(20.dp)
        ) {
            PersonIcon()
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(start = 20.dp)
            ) {

                Text(
                    text = authViewModel.uiState.value.data?.firstName ?: "Nome não encontrado",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = authViewModel.uiState.value.data?.contact ?: "Contato não encontrado",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = authViewModel.uiState.value.data?.email ?: "E-mail não encontrado",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}