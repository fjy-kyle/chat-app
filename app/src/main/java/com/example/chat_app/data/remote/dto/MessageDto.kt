package com.example.chat_app.data.remote.dto

import com.example.chat_app.domain.model.Message
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.Date

@Serializable
data class MessageDto(
    val text: String,
    val timestamp: Long,
    val username: String,
    val id : String
) {
    fun toMessage(): Message {
        val data = Date(timestamp)
        val formattedDate = DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(data)
        return Message(
            text = text,
            formattedTime = formattedDate,
            username = username
        )
    }
}
