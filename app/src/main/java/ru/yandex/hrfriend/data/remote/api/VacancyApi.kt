package ru.yandex.hrfriend.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyResponse
import ru.yandex.hrfriend.data.dto.vacancy.VacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.VacancyResponse
import java.util.UUID

interface VacancyApi {

    private companion object {
        const val PATH = "vacancy"
    }

    @Headers("Content-Type:application/json")
    @POST("$PATH/list")
    suspend fun getAll(@Body pageable: VacancyRequest): Response<VacancyResponse>

    @Headers("Content-Type:application/json")
    @POST(PATH)
    suspend fun add(@Body addVacancyRequest: AddVacancyRequest): Response<AddVacancyResponse>

    @Headers("Content-Type:application/json")
    @DELETE("$PATH/{id}")
    suspend fun delete(@Path("id") id: UUID): Response<Unit>

    @Headers("Content-Type:application/json")
    @PATCH("$PATH/{id}")
    suspend fun update(@Path("id") id: UUID, @Body addVacancyRequest: AddVacancyRequest): Response<AddVacancyResponse>

    @Headers("Content-Type:application/json")
    @POST("$PATH/{id}")
    suspend fun getById(@Path("id") id: UUID): Response<VacancyResponse>

}