package ru.yandex.hrfriend.domain.models

import kotlinx.serialization.Serializable
import ru.yandex.hrfriend.data.dto.chat.MessageDto


data class MessageList(
    val messages: List<MessageDto>
)
