package com.example.todos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todos.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedTimeInfo(isDone: Boolean, onDateSelected: MutableState<Long?>) {
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    Column(
        modifier = Modifier
            .clickable(enabled = !isDone, onClick = { openDialog.value = true })
            .alpha(if (isDone) 0.5f else 1f)
    ) {
        TextWithIcon(title = "Срок выполнения", icon = R.drawable.icon_calendar)
        Row(
            modifier = Modifier
                .height(84.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(if (isDone) Color.LightGray else Color(0xFFF3E8FF))
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = if (isDone) Color.Gray else Color(0xFFC4B5FD)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 18.dp)
                    .size(48.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(if (isDone) Color.Gray else Color(0xFFA78BFA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_calendar),
                    contentDescription = null,
                    tint = if (isDone) Color.DarkGray else Color.Black
                )
            }

            Text(
                text = convertMillisToDate(onDateSelected.value ?: System.currentTimeMillis()),
                color = if (isDone) Color.Gray else Color.Unspecified
            )
        }
    }

    if (openDialog.value && !isDone) {
        DatePickerDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected.value = millis
                    }
                    openDialog.value = false
                }) {
                    Text(text = "Ок")
                }
            },
        ) {
            DatePicker(datePickerState)
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))
    return formatter.format(Date(millis))
}