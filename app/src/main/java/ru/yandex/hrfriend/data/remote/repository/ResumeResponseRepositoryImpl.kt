package ru.yandex.hrfriend.data.remote.repository

import retrofit2.Response
import ru.yandex.hrfriend.data.dto.resume_response.AddResumeResponseRequest
import ru.yandex.hrfriend.data.dto.resume_response.ResumeResponseResponse
import ru.yandex.hrfriend.data.remote.api.ResumeResponseApi
import ru.yandex.hrfriend.domain.repository.ResumeResponseRepository
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import java.util.UUID
import javax.inject.Inject

class ResumeResponseRepositoryImpl  @Inject constructor(
    private val resumeResponseApi: ResumeResponseApi,
    private val preferencesManager: PreferencesManager
) : ResumeResponseRepository {
    override suspend fun addResumeResponse(addResumeResponseRequest: AddResumeResponseRequest): Response<ResumeResponseResponse> {
        return resumeResponseApi.addResumeResponse(
            addResumeResponseRequest,
            Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY))
        )
    }

    override suspend fun deleteResumeResponse(id: UUID): Response<Unit> {
        return resumeResponseApi.deleteResumeResponse(id, Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY)))
    }

    override suspend fun updateStatusResumeResponse(id: UUID): Response<ResumeResponseResponse> {
        return resumeResponseApi.updateStatusResumeResponse(id, Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY)))
    }

    override suspend fun getByVacancyId(id: UUID): Response<List<ResumeResponseResponse>> {
        return resumeResponseApi.getByVacancyId(id, Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY)))
    }

    override suspend fun getByUser(): Response<List<ResumeResponseResponse>> {
        return resumeResponseApi.getByUser(Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY)))
    }

}