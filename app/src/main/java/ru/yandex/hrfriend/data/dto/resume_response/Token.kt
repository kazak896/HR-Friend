package ru.yandex.hrfriend.data.dto.resume_response

data class Token(
    val expired: Boolean,
    val id: String,
    val revoked: Boolean,
    val token: String,
    val tokenType: String,
    val user: String
)