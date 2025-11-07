package com.example.todos

import androidx.compose.ui.graphics.Color
import org.json.JSONObject
import androidx.compose.ui.graphics.toArgb
import org.joda.time.DateTime

val TodoItem.json: JSONObject
    get() {
        return toJsonObject()
    }

private fun TodoItem.toJsonObject() : JSONObject{
    val json = JSONObject()
    json.put(JsonField.UID.value, uid)
    json.put(JsonField.TEXT.value, text)
    json.put(JsonField.IS_DONE.value, isDone)

    if (color != Color.White) {
        color?.let { json.put(JsonField.COLOR.value, it.toArgb()) }
    }

    if (importance != Importance.ORDINARY) {
        json.put(JsonField.IMPORTANCE.value, importance.name)
    }

    if (deadline != null) {
        json.put(JsonField.DEADLINE.value, deadline.millis)
    }

    return json
}

fun JSONObject.parse(): TodoItem? {
    val uid = getString(JsonField.UID.value)
    val text = getString(JsonField.TEXT.value)
    val isDone = getBoolean(JsonField.IS_DONE.value)

    val color = if (has(JsonField.COLOR.value)) {
        Color(getInt(JsonField.COLOR.value))
    } else {
        Color.White
    }

    val importance = if (has(JsonField.IMPORTANCE.value)) {
        Importance.valueOf(getString(JsonField.IMPORTANCE.value))
    } else {
        Importance.ORDINARY
    }

    val deadline = if (has(JsonField.DEADLINE.value)) {
        DateTime(getLong(JsonField.DEADLINE.value))
    } else {
        null
    }

    return TodoItem(
        uid = uid,
        text = text,
        importance = importance,
        color = color,
        deadline = deadline,
        isDone = isDone
    )
}

private enum class JsonField(val value: String) {
    UID("uid"),
    TEXT("text"),
    IS_DONE("isDone"),
    COLOR("color"),
    IMPORTANCE("importance"),
    DEADLINE("deadline")
}