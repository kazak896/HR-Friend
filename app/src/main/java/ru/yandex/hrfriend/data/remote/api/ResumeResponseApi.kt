package ru.yandex.hrfriend.data.remote.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import ru.yandex.hrfriend.data.dto.resume_response.AddResumeResponseRequest
import ru.yandex.hrfriend.data.dto.resume_response.ResumeResponseResponse
import java.util.UUID

interface ResumeResponseApi {
    private companion object {
        const val PATH = "resume-response"
    }

    @Headers("Content-Type:application/json")
    @POST(PATH)
    suspend fun addResumeResponse(addResumeResponseRequest: AddResumeResponseRequest, @Header("Authorization") token: String) : Response<ResumeResponseResponse>

    @Headers("Content-Type:application/json")
    @DELETE("$PATH/{id}")
    suspend fun deleteResumeResponse(@Path("id") id : UUID, @Header("Authorization") token: String) : Response<Unit>

    @Headers("Content-Type:application/json")
    @PATCH("$PATH/{id}")
    suspend fun updateStatusResumeResponse(@Path("id") id : UUID, @Header("Authorization") token: String) : Response<ResumeResponseResponse>

    @Headers("Content-Type:application/json")
    @GET("$PATH/vacancy/{id}")
    suspend fun getByVacancyId(@Path("id") id : UUID, @Header("Authorization") token: String) : Response<List<ResumeResponseResponse>>

    @Headers("Content-Type:application/json")
    @GET("$PATH/user")
    suspend fun getByUser(@Header("Authorization") token: String) : Response<List<ResumeResponseResponse>>

}