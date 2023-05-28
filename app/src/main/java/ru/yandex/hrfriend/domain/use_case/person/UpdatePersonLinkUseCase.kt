package ru.yandex.hrfriend.domain.use_case.person

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.yandex.hrfriend.data.dto.ErrorResponse
import ru.yandex.hrfriend.data.dto.person.ResumePath
import ru.yandex.hrfriend.domain.repository.PersonRepository
import ru.yandex.hrfriend.domain.repository.ResumeResponseRepository
import ru.yandex.hrfriend.util.Resource
import java.util.UUID
import javax.inject.Inject

class UpdatePersonLinkUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    operator fun invoke(
        link : String
    ) : Flow<Resource<Unit>> = flow {
        try {
            val response = personRepository.updatePersonResumeLink(ResumePath(link))
            val result = response.body()
            if (response.isSuccessful && result != null) {
                emit(Resource.Success(result))
            }else {
                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type
                val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                if (errorResponse != null) {
                    emit(Resource.Error(errorResponse.description))
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }
}