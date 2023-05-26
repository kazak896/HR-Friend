package ru.yandex.hrfriend.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import ru.yandex.hrfriend.data.dto.vacancy.VacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.VacancyResponse
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyType
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyTypeDto
import java.util.UUID

interface VacancyTypeApi {

    private companion object {
        const val PATH = "vacancy-type"
    }

    @Headers("Content-Type:application/json")
    @GET(PATH)
    suspend fun getAll(): Response<List<VacancyType>>

    @Headers("Content-Type:application/json")
    @POST(PATH)
    suspend fun add(@Body vacancyTypeDto: VacancyTypeDto) : Response<VacancyType>

    @Headers("Content-Type:application/json")
    @DELETE("$PATH/{id}")
    suspend fun delete(@Path("id") id: UUID) : Response<Unit>


}