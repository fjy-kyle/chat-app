package com.example.chat_app.data.remote

import com.example.chat_app.data.remote.dto.MessageDto
import com.example.chat_app.domain.model.Message
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.utils.io.*

class MessageServiceImpl(
    private val client: HttpClient,
): MessageService {

    override suspend fun getAllMessage(): List<Message> {
        return try {
            client.get<List<MessageDto>>(MessageService.Endpoints.GetALlMessages.url)
                .map { it.toMessage() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}