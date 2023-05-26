package ru.yandex.hrfriend.data.dto.vacancy

data class Candidate(
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val authorities: List<Authority>,
    val credentialsNonExpired: Boolean,
    val email: String,
    val enabled: Boolean,
    val firstname: String,
    val id: String,
    val lastname: String,
    val password: String,
    val resumePath: String,
    val resumeResponses: List<String>,
    val role: String,
    val tokens: List<Token>,
    val username: String
)