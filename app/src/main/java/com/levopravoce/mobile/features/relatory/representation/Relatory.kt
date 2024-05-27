package com.levopravoce.mobile.features.relatory.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
//    RenderRow(
//        listOf(
//            data.clientName,
//            formatCurrency(data.value),
//            data.deliveryDate.format(
//                DateTimeFormatter.ofPattern("dd/MM/yyyy")
//            )
//        )
//    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInput() {
    val dateState = rememberDatePickerState()
    DatePicker(
        state = dateState
    )
//    val dateState = rememberDatePickerState()
//    val millisToLocalDate = dateState.selectedDateMillis?.let {
//        DateUtils.convertMillisToLocalDate(it)
//    }
//    val dateToString = millisToLocalDate?.let {
//        DateUtils.dateToString(millisToLocalDate)
//    } ?: ""
//    Column {
//        DatePicker(
//            title = {
//                Text(text = "Manufactured Date")
//            },
//            headline = { Text(text = "Car's manufactured date")},
//            state = dateState,
//            showModeToggle = true
//        )
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            text = dateToString
//        )
//    }
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