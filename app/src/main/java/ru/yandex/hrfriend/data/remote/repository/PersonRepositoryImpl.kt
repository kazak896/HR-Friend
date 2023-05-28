package ru.yandex.hrfriend.data.remote.repository

import retrofit2.Response
import ru.yandex.hrfriend.data.dto.person.ResumePath
import ru.yandex.hrfriend.data.remote.api.PersonApi
import ru.yandex.hrfriend.domain.repository.PersonRepository
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val personApi: PersonApi,
    private val preferencesManager: PreferencesManager
) : PersonRepository {
    override suspend fun updatePersonResumeLink(resumePath: ResumePath): Response<Unit> {
        return personApi.updatePersonResumeLink(resumePath, Constants.getAuthHeader(preferencesManager.getString(Constants.JWT_KEY)))
    }
}