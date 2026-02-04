package com.example.todos.data.storage

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID
import androidx.core.content.edit


object DeviceIdStorage {
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.applicationContext.getSharedPreferences("device_id", Context.MODE_PRIVATE)
    }

    val deviceId: String
        get() {
            var id = sharedPreferences.getString("device_id", null)

            if (id == null) {
                id = UUID.randomUUID().toString()
                sharedPreferences.edit { putString("device_id", id) }
            }
            return id
        }
}