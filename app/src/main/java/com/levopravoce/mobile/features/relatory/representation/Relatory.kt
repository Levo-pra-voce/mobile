package com.levopravoce.mobile.features.relatory.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.DateUtils
import com.levopravoce.mobile.common.formatCurrency
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.start.representation.bottomBorder
import com.levopravoce.mobile.ui.theme.White100
import com.levopravoce.mobile.ui.theme.customColorsShema
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class MockData(
    val clientName: String,
    val deliveryDate: LocalDate,
    val value: Double,
)

// columns = nome do cliente, valor e data de entrega

@Composable
fun Relatory() {
    val mockList = List(10) {
        MockData(
            clientName = "Cliente $it",
            deliveryDate = LocalDate.now(),
            value = it * 100.0
        )
    }

    Screen(
        padding = 0.dp
    ) {
        Header(
            horizontal = Alignment.Start
        ) {
            Row {
                BackButton(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                )
                Text(
                    text = "Relatórios",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }

        Column {
            DatePickerInput()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ButtonStyled(
                text = "Exportar",
                onClick = {}
            )
            ButtonStyled(
                text = "Buscar",
                onClick = {}
            )
        }
        LazyColumn {
            items(mockList.size) { item ->
                RenderRelatoryItem(mockList[item], item == mockList.size - 1)
            }
        }
    }
}

@Composable
fun RenderRelatoryItem(data: MockData, isLast: Boolean) {
    var rowModifier = Modifier
        .padding(top = 24.dp, start = 8.dp)
        .fillMaxWidth()

    if (!isLast) {
        rowModifier = rowModifier.bottomBorder(1.dp, White100)
    }
    Row(
        modifier = rowModifier
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.person_icon),
                contentDescription = "icone da pessoa",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
        Column {
            Text(text = data.clientName, color = MaterialTheme.customColorsShema.placeholder)
            Text(
                text = "Valor: ${formatCurrency(data.value)}",
                color = MaterialTheme.customColorsShema.placeholder
            )
            Text(
                text = "Data da entrega: ${data.deliveryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                color = MaterialTheme.customColorsShema.placeholder
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInput() {
    val currentYear = LocalDate.now().year

    val dateState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = LocalDate.now().plusDays(1).atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli(),
        yearRange = currentYear..currentYear,
    )

    LaunchedEffect(key1 = dateState.selectedDateMillis) {
        val millisToLocalDate = dateState.selectedDateMillis?.let {
            DateUtils.convertMillisToLocalDate(it)
        }
    }

    Column {
        DatePicker(
            state = dateState,
            showModeToggle = true,
            colors = DatePickerDefaults.colors(
                dateTextFieldColors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.customColorsShema.title,
                    unfocusedTextColor = MaterialTheme.customColorsShema.title,
                    disabledTextColor = MaterialTheme.customColorsShema.title,
                    errorTextColor = MaterialTheme.customColorsShema.title,
                    focusedContainerColor = MaterialTheme.customColorsShema.background,
                    unfocusedContainerColor = MaterialTheme.customColorsShema.background,
                    disabledContainerColor = MaterialTheme.customColorsShema.background,
                    errorContainerColor = MaterialTheme.customColorsShema.background,
                    focusedPlaceholderColor = MaterialTheme.customColorsShema.placeholder,
                    unfocusedPlaceholderColor = MaterialTheme.customColorsShema.placeholder,
                    focusedLabelColor = MaterialTheme.customColorsShema.title,
                    focusedIndicatorColor = MaterialTheme.customColorsShema.title,
                ),
                selectedDayContentColor = MaterialTheme.customColorsShema.title,
                selectedDayContainerColor = MaterialTheme.customColorsShema.invertBackground,
                titleContentColor = MaterialTheme.customColorsShema.title,
                todayContentColor = MaterialTheme.customColorsShema.title,
                headlineContentColor = MaterialTheme.customColorsShema.title,
            )
        )
    }
}

@Composable
fun ButtonStyled(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .width(100.dp)
            .clip(RoundedCornerShape(20))
            .background(MaterialTheme.customColorsShema.invertBackground)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.customColorsShema.title
            )
        }
    }
}

@Composable
fun RenderRow(data: List<String>) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        data.forEach {
            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .border(1.dp, MaterialTheme.customColorsShema.placeholder),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.customColorsShema.title,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}