package ru.yandex.hrfriend.data.dto.vacancy

import java.util.UUID

data class Token(
    val expired: Boolean,
    val id: UUID,
    val revoked: Boolean,
    val token: String,
    val tokenType: String,
    val user: String
)