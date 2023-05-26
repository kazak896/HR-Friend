package ru.yandex.hrfriend.data.dto.sign_in_up

data class LoginRequest(
    val email : String,
    val password : String
)

class InvalidLoginRequestException(message: String): Exception(message)