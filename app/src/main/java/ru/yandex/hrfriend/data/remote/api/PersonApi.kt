package ru.yandex.hrfriend.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import ru.yandex.hrfriend.data.dto.person.ResumePath

interface PersonApi {

    private companion object {
        const val PATH = "user"
    }

    @Headers("Content-Type:application/json")
    @PATCH(PATH)
    suspend fun updatePersonResumeLink(@Body resumePath: ResumePath, @Header("Authorization") token: String): Response<Unit>

}