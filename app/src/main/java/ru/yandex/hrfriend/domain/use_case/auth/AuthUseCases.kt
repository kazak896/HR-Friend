package ru.yandex.hrfriend.domain.use_case.auth

data class AuthUseCases(
    val appLoginUseCase: AppLoginUseCase,
    val appSignUpUseCase: AppSignUpUseCase
)
