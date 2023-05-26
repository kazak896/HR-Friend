package ru.yandex.hrfriend.domain.repository

import retrofit2.Response
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyType
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyTypeDto
import java.util.UUID

interface VacancyTypeRepository {

    suspend fun getAll(): Response<List<VacancyType>>

    suspend fun add(vacancyTypeDto: VacancyTypeDto) : Response<VacancyType>

    suspend fun delete(id: UUID) : Response<Unit>
}