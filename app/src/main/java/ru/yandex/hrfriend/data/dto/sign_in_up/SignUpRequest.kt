package ru.yandex.hrfriend.data.dto.sign_in_up

data class SignUpRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String
)
