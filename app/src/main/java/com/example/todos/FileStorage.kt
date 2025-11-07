package com.example.todos

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FileStorage(context: Context) {
    private val _todoItems = mutableStateListOf<TodoItem>()
    val todoItems: List<TodoItem> get() = _todoItems
    private val file = context.getSharedPreferences("todos", Context.MODE_PRIVATE)
    private val key = "todo_items"

    private val log = LoggerFactory.getLogger(FileStorage::class.java)

    fun addNewTodo(todoItem: TodoItem) {
        _todoItems.add(todoItem)
        log.info("Добавляем новую задачу: ${todoItem.text}, всего задач: " + _todoItems.size)
        saveTodosToFile()
    }

    fun deleteTodo(uid: String) {
        _todoItems.removeIf { it.uid == uid }
        log.info("Удаляем задачу: ${uid}, осталось задач: " + _todoItems.size)
        saveTodosToFile()
    }

    fun saveTodosToFile() {
        val jsonArray = JSONArray(_todoItems.map(TodoItem::json))
        file.edit().putString(key, jsonArray.toString()).apply()
        log.info("Сохраняем задачи в файл, всего задач: " + _todoItems.size)
    }

    fun loadTodosFromFile() {
        val jsonString = file.getString(key, "[]")
        val jsonArray = JSONArray(jsonString)

        _todoItems.clear()

        val list = (0 until jsonArray.length())
            .mapNotNull { jsonItem ->
                val jsonObject = jsonArray.getJSONObject(jsonItem)
                jsonObject.parse()
            }
        _todoItems.addAll(list)
        log.info("Загружаем задачи из файла, всего задач: " + _todoItems.size)
        _todoItems.forEach { todo ->
            log.info("'${todo.text}' (${todo.importance})")
        }
    }

    fun updateTodo(updatedTodo: TodoItem) {
        val index = _todoItems.indexOfFirst { it.uid == updatedTodo.uid }
        if (index != -1) {
            _todoItems[index] = updatedTodo
            saveTodosToFile()
        }
    }
}