package ru.yandex.hrfriend.presentation.chat

import ru.yandex.hrfriend.domain.models.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
)