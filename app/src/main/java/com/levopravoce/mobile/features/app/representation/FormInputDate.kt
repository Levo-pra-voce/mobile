package com.levopravoce.mobile.features.app.representation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.levopravoce.mobile.common.DateUtils
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputDate(
    value: String,
    placeHolder: String = "",
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit,
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    Column {
        FormInputText(
            value = value,
            placeHolder = placeHolder,
            enabled = false,
            withBorder = false,
            onChange = {},
            modifier = Modifier
                .then(modifier)
                .clickable {
                    showDatePickerDialog = true
                })
        if (showDatePickerDialog) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePickerDialog = false },
                confirmButton = {
                    Button(text = "Confirmar") {
                        datePickerState
                            .selectedDateMillis?.let {
                                val date = DateUtils.convertMillisToLocalDate(it)
                                val dateWithPlusOneDay = date.plusDays(1)
                                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                onDateSelected(dateWithPlusOneDay.format(dateFormatter))
                            }
                        showDatePickerDialog = false
                    }
                },
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }
}