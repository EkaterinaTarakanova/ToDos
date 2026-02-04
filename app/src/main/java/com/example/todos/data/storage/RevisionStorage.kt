package com.example.todos.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object RevisionStorage {
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences =
            context.applicationContext.getSharedPreferences("revision", Context.MODE_PRIVATE)
    }

    fun getRevision(): Int = sharedPreferences.getInt("revision", 0)

    fun saveRevision(revision: Int) {
        sharedPreferences.edit { putInt("revision", revision) }
    }
}