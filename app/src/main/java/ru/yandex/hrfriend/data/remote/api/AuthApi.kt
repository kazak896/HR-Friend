package ru.yandex.hrfriend.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.yandex.hrfriend.data.dto.sign_in_up.LoginRequest
import ru.yandex.hrfriend.data.dto.sign_in_up.LoginResponse
import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpRequest
import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpResponse

interface AuthApi {

    private companion object {
        const val PATH = "auth"
    }

    @Headers("Content-Type:application/json")
    @POST("${PATH}/authenticate")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("${PATH}/register")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>
}