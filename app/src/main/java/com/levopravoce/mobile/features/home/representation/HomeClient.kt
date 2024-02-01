package com.levopravoce.mobile.features.home.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.R
import com.levopravoce.mobile.features.auth.domain.AuthViewModel
import com.levopravoce.mobile.features.home.data.IconDescriptorData
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch


private val firstLineDescriptorData = listOf(
    IconDescriptorData(
        id = R.drawable.delivery_ok,
        contentDescription = "icone para solicitar entrega",
        title = "Solicitar\n" +
                "entrega"
    ),
    IconDescriptorData(
        id = R.drawable.delivery_call,
        contentDescription = "icone para acompanhar entrega",
        title = "Acompanhar\n" +
                "entrega"
    ),
)

private val secondLineDescriptorData = listOf(
    IconDescriptorData(
        id = R.drawable.delivery_persons_icon,
        contentDescription = "icone para para listagem de motoristas",
        title = "Motoras"
    ),
    IconDescriptorData(
        id = R.drawable.message_icon,
        contentDescription = "icone para ver a lista de mensagens",
        title = "Mensagens"
    ),
)

@Composable
fun HomeClient() {
    Column {
        UserHeader()
        UserOptions()
    }
}

@Composable
fun RowOption(
    horizontalArrangement: Arrangement.Horizontal,
    iconDescriptorData: List<IconDescriptorData>,
    modifier: Modifier = Modifier
) {
    val navController = navControllerContext.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
    ) {
        iconDescriptorData.forEach {
            IconDescriptor(
                id = it.id,
                contentDescription = it.contentDescription,
                title = it.title,
                onClick = {
                    if (it.onClick != null) {
                        it.onClick.invoke()
                    } else {
                        it.route?.let { route ->
                            navController?.navigate(route)
                        }
                    }
                },
                imageModifier = it.imageModifier
            )
        }
    }
}

@Composable
private fun UserOptions(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val thirdLineDescriptorData = listOf(
        IconDescriptorData(
            id = R.drawable.exit_icon,
            contentDescription = "icone para sair do aplicativo",
            title = "Sair",
            imageModifier = Modifier.offset(x = -(10.dp)),
            onClick = {
                coroutineScope.launch {
                    authViewModel.logout()
                }
            }
        ),
        IconDescriptorData(
            id = R.drawable.configuration_icon,
            contentDescription = "icone para ver as configurações",
            title = "Configurações",
            route = Routes.Home.CONFIGURATION
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.customColorsShema.background)
            .padding(36.dp)
    ) {
        RowOption(
            horizontalArrangement = Arrangement.SpaceBetween,
            iconDescriptorData = firstLineDescriptorData,
            modifier = Modifier
        )
        RowOption(
            horizontalArrangement = Arrangement.SpaceBetween,
            iconDescriptorData = secondLineDescriptorData,
            modifier = Modifier.padding(top = 108.dp)
        )
        RowOption(
            horizontalArrangement = Arrangement.SpaceBetween,
            iconDescriptorData = thirdLineDescriptorData,
            modifier = Modifier.padding(top = 108.dp)
        )
    }
}


@Preview
@Composable
fun UserOptionsPreview() {
    UserOptions()
}