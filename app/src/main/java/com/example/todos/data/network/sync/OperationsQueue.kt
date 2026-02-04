package com.example.todos.data.network.sync

object OperationsQueue {
    private val operations = mutableListOf<UnsyncedOperations>()

    fun add(operation: UnsyncedOperations) {
        operations.add(operation)
    }

    fun get(): List<UnsyncedOperations> = operations.toList()

    fun removeFirst(): UnsyncedOperations? =
        if (operations.isNotEmpty()) operations.removeAt(0) else null

    fun peek(): UnsyncedOperations? =
        if (operations.isNotEmpty()) operations.get(0) else null

    fun clear() = operations.clear()
}