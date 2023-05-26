package ru.yandex.hrfriend.data.remote.repository

import retrofit2.Response
import ru.yandex.hrfriend.data.dto.sign_in_up.LoginRequest
import ru.yandex.hrfriend.data.dto.sign_in_up.LoginResponse
import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpRequest
import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpResponse
import ru.yandex.hrfriend.data.remote.api.AuthApi
import ru.yandex.hrfriend.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
     private val authApi: AuthApi
) : AuthRepository {
    override suspend fun breslerAppLogin(loginRequest: LoginRequest): Response<LoginResponse> {
        return authApi.login(loginRequest)
    }
    override suspend fun breslerAppSignUp(signUpRequest: SignUpRequest): Response<SignUpResponse> {
        return authApi.signUp(signUpRequest)
    }
}