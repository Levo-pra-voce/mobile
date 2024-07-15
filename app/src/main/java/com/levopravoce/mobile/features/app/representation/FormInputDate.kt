package com.levopravoce.mobile.features.app.representation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.levopravoce.mobile.common.DateUtils
import com.levopravoce.mobile.ui.theme.customColorsShema
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputDate(
    value: String,
    label: String? = null,
    labelModifier: Modifier = Modifier,
    placeHolder: String = "",
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit,
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    Box {
        if (label != null) {
            Text(
                text = label,
                color = MaterialTheme.customColorsShema.title,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                modifier = Modifier
                    .offset(y = 12.dp)
                    .zIndex(1f)
                    .then(labelModifier)
            )
        }
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
                }
                .padding(top = if (label != null) 4.dp else 0.dp))
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