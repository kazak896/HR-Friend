package ru.yandex.hrfriend.data.remote.repository

import retrofit2.Response
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyResponse
import ru.yandex.hrfriend.data.dto.vacancy.VacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.VacancyResponse
import ru.yandex.hrfriend.data.remote.api.VacancyApi
import ru.yandex.hrfriend.domain.repository.VacancyRepository
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import java.util.UUID
import javax.inject.Inject

class VacancyRepositoryImpl @Inject constructor(
    private val vacancyApi: VacancyApi,
    private val preferencesManager: PreferencesManager
) : VacancyRepository{
    override suspend fun getAll(vacancyRequest: VacancyRequest): Response<VacancyResponse> {
        return vacancyApi.getAll(
            vacancyRequest,
            Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY))
        )
    }

    override suspend fun add(addVacancyRequest: AddVacancyRequest): Response<AddVacancyResponse> {
        return vacancyApi.add(
            addVacancyRequest,
            Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY))
        )
    }

    override suspend fun delete(id: UUID): Response<Unit> {
        return vacancyApi.delete(
            id,
            Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY))
        )
    }

    override suspend fun update(
        id: UUID,
        addVacancyRequest: AddVacancyRequest
    ): Response<AddVacancyResponse> {
        return vacancyApi.update(
            id,
            addVacancyRequest,
            Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY))
        )
    }

    override suspend fun getById(id: UUID): Response<VacancyResponse> {
        return vacancyApi.getById(
            id,
            Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY))
        )
    }
}