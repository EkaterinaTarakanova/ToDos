package com.example.todos.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import org.slf4j.LoggerFactory

class FileStorage(context: Context) {
    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> = _todoItems.asStateFlow()
    private val file = context.getSharedPreferences("todos", Context.MODE_PRIVATE)
    private val key = "todo_items"

    private val log = LoggerFactory.getLogger(FileStorage::class.java)

    fun addNewTodo(todoItem: TodoItem) {
        _todoItems.update { currentList ->
            currentList + todoItem
        }
        log.info("Добавляем новую задачу: ${todoItem.text}, всего задач: " + _todoItems.value.size)
        saveTodosToFile()
    }

    fun deleteTodo(uid: String) {
        _todoItems.update { currentList ->
            currentList.filterNot { it.uid == uid }
        }
        log.info("Удаляем задачу: ${uid}, осталось задач: " + _todoItems.value.size)
        saveTodosToFile()
    }

    fun saveTodosToFile() {
        val jsonArray = JSONArray(_todoItems.value.map(TodoItem::json))
        file.edit().putString(key, jsonArray.toString()).apply()
        log.info("Сохраняем задачи в файл, всего задач: " + _todoItems.value.size)
    }

    fun loadTodosFromFile() {
        val jsonString = file.getString(key, "[]")
        val jsonArray = JSONArray(jsonString)

        val list = (0 until jsonArray.length())
            .mapNotNull { jsonItem ->
                val jsonObject = jsonArray.getJSONObject(jsonItem)
                jsonObject.parse()
            }

        _todoItems.update { list }

        log.info("Загружаем задачи из файла, всего задач: " + _todoItems.value.size)
        list.forEach { todo ->
            log.info("'${todo.text}' (${todo.importance})")
        }
    }

    fun updateTodo(updatedTodo: TodoItem) {
        _todoItems.update { currentList ->
            currentList.map { if (it.uid == updatedTodo.uid) updatedTodo else it}
        }
        log.info("Обновляем задачу: ${updatedTodo.text}, ${updatedTodo.isDone}")
        saveTodosToFile()
    }
}