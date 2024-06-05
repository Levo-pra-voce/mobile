package com.levopravoce.mobile.features.user.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.user.domain.UserViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import kotlinx.coroutines.launch

@Composable
fun SubmitButton(
    userViewModel: UserViewModel,
    userDTO: UserDTO = UserDTO(),
    userType: UserType = UserType.CLIENTE
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = navControllerContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (!userViewModel.isLoading()) {
                    coroutineScope.launch {
                        if (userDTO.id == null) {
                            userViewModel.register(userType, userDTO)
                        } else {
                            val success = userViewModel.update(userDTO)
                            if (success) {
                                navController?.navigate(Routes.Auth.LOGIN)
                            }
                        }
                    }
                }
            },
            text = if (userDTO.id == null) "Cadastrar" else "Atualizar",
            modifier = Modifier.width(200.dp)
        )
    }
}