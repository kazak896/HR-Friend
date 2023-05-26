package ru.yandex.hrfriend.domain.repository

import retrofit2.Response
import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpRequest
import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpResponse
import ru.yandex.hrfriend.data.dto.sign_in_up.LoginRequest
import ru.yandex.hrfriend.data.dto.sign_in_up.LoginResponse

interface AuthRepository {

    suspend fun breslerAppLogin(loginRequest: LoginRequest) : Response<LoginResponse>

    suspend fun breslerAppSignUp(signUpRequest: SignUpRequest) : Response<SignUpResponse>

}