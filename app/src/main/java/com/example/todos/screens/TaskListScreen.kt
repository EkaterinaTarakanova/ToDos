package com.example.todos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todos.Importance
import com.example.todos.TaskListViewModelFactory
import com.example.todos.TodoItem
import com.example.todos.components.convertMillisToDate
import com.example.todos.viewModels.TaskListViewModel

@Composable
fun TaskListScreen(
    onAddTaskClick: () -> Unit,
    taskListViewModelFactory: TaskListViewModelFactory,
    onTaskClick: (String) -> Unit,
) {
    val viewModel: TaskListViewModel = viewModel(factory = taskListViewModelFactory)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 35.dp)
            .background(
                color = Color(0xFFE0F2FE)
            )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Home, contentDescription = null)
                Text(text = "Мои дела", style = TextStyle(fontSize = 30.sp))
            }
            LazyColumn(modifier = Modifier.padding(vertical = 20.dp)) {
                items(items = viewModel.todosList, key = { it.uid }) { item ->
                    TodoItemDrawer(
                        todoItem = item,
                        onCheckedChanged = { viewModel.toggleStateChange(todoItem = item) },
                        onTaskClick = { onTaskClick(item.uid) },
                        onTaskDelete = { viewModel.deleteTodoItem(todoItem = item) })

                }
            }
        }
        FloatingActionButton(
            onClick = onAddTaskClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Composable
fun TodoItemDrawer(
    todoItem: TodoItem,
    onCheckedChanged: (Boolean) -> Unit,
    onTaskClick: (String) -> Unit,
    onTaskDelete: (String) -> Unit
) {
    val swipeToDismissBoxState =
        rememberSwipeToDismissBoxState(confirmValueChange = { dismiss ->
            if (dismiss == SwipeToDismissBoxValue.EndToStart) {
                onTaskDelete(todoItem.uid)
                true
            } else {
                false
            }
        })


    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .clickable { onTaskClick(todoItem.uid) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .size(20.dp)
                            .background(color = todoItem.color ?: Color.White)
                            .border(width = 2.dp, color = Color.Black, shape = CircleShape)
                    )
                    Text(
                        modifier = Modifier
                            .width(250.dp)
                            .wrapContentHeight()
                            .padding(start = 10.dp), text = todoItem.text
                    )
                    Checkbox(checked = todoItem.isDone, onCheckedChange = onCheckedChanged)

                    IconButton(onClick = {onTaskDelete(todoItem.uid)}) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ImportanceMark(importance = todoItem.importance, todoItem = todoItem)
                    todoItem.deadline?.let { DeadlineMark(deadlineMillis = it.millis) }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(top = 10.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
        }
    )
}

@Composable
fun ImportanceMark(importance: Importance, todoItem: TodoItem) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = importance.color)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            text = todoItem.importance.russianName,
            style = TextStyle(color = Color.White)
        )
    }
}

@Composable
fun DeadlineMark(deadlineMillis: Long) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFF90CAF9),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = convertMillisToDate(deadlineMillis),
                color = Color(0xFF1976D2),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
