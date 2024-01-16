package com.levopravoce.mobile.features.home.representation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.R
import com.levopravoce.mobile.features.auth.domain.AuthViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema


data class IconDescriptorData(
    @DrawableRes val id: Int,
    val contentDescription: String,
    val title: String,
)

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

private val thirdLineDescriptorData = listOf(
    IconDescriptorData(
        id = R.drawable.configuration_icon,
        contentDescription = "icone para ver as configurações",
        title = "Configurações"
    ),
)

@Composable
fun HomeClient(
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val uiState = authViewModel.uiState.collectAsState();

    Column {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.customColorsShema.invertBackground)
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
        UserOptions()
    }
}

@Composable
fun RowOption(
    horizontalArrangement: Arrangement.Horizontal,
    iconDescriptorData: List<IconDescriptorData>,
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
    ) {
        iconDescriptorData.forEach {
            IconDescriptor(
                id = it.id,
                contentDescription = it.contentDescription,
                title = it.title
            )
        }
    }
}

@Composable
fun UserOptions() {
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
            modifier = Modifier.padding(top = 128.dp)
        )
        RowOption(
            horizontalArrangement = Arrangement.Center,
            iconDescriptorData = thirdLineDescriptorData,
            modifier = Modifier.padding(top = 48.dp)
        )
    }
}


@Preview
@Composable
fun UserOptionsPreview() {
    UserOptions()
}