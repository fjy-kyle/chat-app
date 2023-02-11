package com.example.chat_app.data.remote

import com.example.chat_app.domain.model.Message

interface MessageService {

    suspend fun getAllMessage() : List<Message>

    companion object {
        private const val IP_ADDRESS = "47.108.253.91"
        private const val PORT = "8088"
        const val BASE_URL = "http://$IP_ADDRESS:$PORT"

    }

    sealed class Endpoints(val url: String) {
        object GetALlMessages: Endpoints("$BASE_URL/messages")
    }
}