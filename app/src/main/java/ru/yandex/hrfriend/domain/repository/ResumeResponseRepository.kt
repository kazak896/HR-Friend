package ru.yandex.hrfriend.domain.repository

import retrofit2.Response
import ru.yandex.hrfriend.data.dto.resume_response.AddResumeResponseRequest
import ru.yandex.hrfriend.data.dto.resume_response.ResumeResponseResponse
import java.util.UUID

interface ResumeResponseRepository {

    suspend fun addResumeResponse(addResumeResponseRequest: AddResumeResponseRequest): Response<ResumeResponseResponse>

    suspend fun deleteResumeResponse(id: UUID): Response<Unit>

    suspend fun updateStatusResumeResponse(id: UUID): Response<ResumeResponseResponse>

    suspend fun getByVacancyId(id: UUID): Response<List<ResumeResponseResponse>>

    suspend fun getByUser(): Response<List<ResumeResponseResponse>>
}