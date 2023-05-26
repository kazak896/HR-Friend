package ru.yandex.hrfriend.data.remote.repository

import retrofit2.Response
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyResponse
import ru.yandex.hrfriend.data.dto.vacancy.VacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.VacancyResponse
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyType
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyTypeDto
import ru.yandex.hrfriend.data.remote.api.VacancyTypeApi
import ru.yandex.hrfriend.domain.repository.VacancyTypeRepository
import java.util.UUID
import javax.inject.Inject

class VacancyTypeRepositoryImpl @Inject constructor(
    private val vacancyTypeApi: VacancyTypeApi
) : VacancyTypeRepository {
    override suspend fun getAll(): Response<List<VacancyType>> {
        return vacancyTypeApi.getAll()
    }

    override suspend fun add(vacancyTypeDto: VacancyTypeDto): Response<VacancyType> {
        return vacancyTypeApi.add(vacancyTypeDto)
    }

    override suspend fun delete(id: UUID): Response<Unit> {
        return vacancyTypeApi.delete(id)
    }

}