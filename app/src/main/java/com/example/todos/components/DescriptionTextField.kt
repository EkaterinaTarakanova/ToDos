package com.example.todos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todos.R

@Composable
fun DescriptionTextField(isDone: Boolean) {
    val inputText = remember { mutableStateOf("") }
    val textStyle = LocalTextStyle.current

    Column(modifier = Modifier.alpha(if (isDone) 0.5f else 1f)) {
        TextWithIcon(title = "Описание", icon = R.drawable.description_icon)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 200.dp)
                .background(
                    color = if (isDone) Color.LightGray else Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isDone) Color.Gray else Color(0xFFC4B5FD),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            BasicTextField(
                value = inputText.value,
                onValueChange = { if (!isDone) inputText.value = it },
                textStyle = textStyle.copy(color = if (isDone) Color.Gray else Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                enabled = !isDone,
                maxLines = 10,
                decorationBox = { innerTextField ->
                    innerTextField()
                }
            )
        }
    }
}
