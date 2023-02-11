package com.example.chat_app.presentation.chat

import com.example.chat_app.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
)
