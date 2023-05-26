package ru.yandex.hrfriend.domain.models

data class Message(
    val text: String,
    val formattedTime: String,
    val username: String
) {
    override fun toString(): String {
        return "Message(text='$text', formattedTime='$formattedTime', username='$username')"
    }
}
