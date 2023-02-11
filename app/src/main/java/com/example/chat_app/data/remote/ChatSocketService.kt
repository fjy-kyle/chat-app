package com.example.chat_app.data.remote

import com.example.chat_app.domain.model.Message
import com.example.chat_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(
        username: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessage(): Flow<Message>

    suspend fun closeSession()

    companion object {
        private const val IP_ADDRESS = "47.108.253.91"
        private const val PORT = "8088"
        const val BASE_URL = "ws://$IP_ADDRESS:$PORT"

    }

    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }
}