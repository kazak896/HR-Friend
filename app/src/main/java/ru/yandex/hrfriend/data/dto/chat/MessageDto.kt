package ru.yandex.hrfriend.data.dto.chat

import kotlinx.serialization.Serializable
import ru.yandex.hrfriend.domain.models.Message
import java.text.DateFormat
import java.util.Date
import java.util.UUID

data class MessageDto(
    val text: String,
    val timestamp: Long,
    val username: String,
    val id: UUID
) {
    fun toMessage(): Message {
        val date = Date(timestamp)
        val formattedDate = DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(date)
        return Message(
            text = text,
            formattedTime = formattedDate,
            username = username
        )
    }

    override fun toString(): String {
        return "MessageDto(text='$text', timestamp=$timestamp, username='$username', id='$id')"
    }

}
