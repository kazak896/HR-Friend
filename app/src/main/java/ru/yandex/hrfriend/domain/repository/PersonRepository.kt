package ru.yandex.hrfriend.domain.repository

import retrofit2.Response
import ru.yandex.hrfriend.data.dto.person.ResumePath

interface PersonRepository {
    suspend fun updatePersonResumeLink(resumePath: ResumePath): Response<Unit>
}