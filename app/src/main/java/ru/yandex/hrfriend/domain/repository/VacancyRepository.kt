package ru.yandex.hrfriend.domain.repository

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
import ru.yandex.hrfriend.data.remote.api.VacancyApi
import java.util.UUID

interface VacancyRepository {

    suspend fun getAll(vacancyRequest: VacancyRequest): Response<VacancyResponse>

    suspend fun add(addVacancyRequest: AddVacancyRequest): Response<AddVacancyResponse>

    suspend fun delete(id: UUID): Response<Unit>

    suspend fun update(id: UUID, addVacancyRequest: AddVacancyRequest): Response<AddVacancyResponse>

    suspend fun getById(id: UUID) : Response<VacancyResponse>

}