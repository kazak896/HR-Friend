package ru.yandex.hrfriend.domain.use_case.resume_response

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.yandex.hrfriend.data.dto.ErrorResponse
import ru.yandex.hrfriend.data.dto.resume_response.AddResumeResponseRequest
import ru.yandex.hrfriend.data.dto.resume_response.ResumeResponseResponse
import ru.yandex.hrfriend.domain.repository.ResumeResponseRepository
import ru.yandex.hrfriend.util.Resource
import javax.inject.Inject

class AddResumeResponseUseCase @Inject constructor(
    private val resumeResponseRepository: ResumeResponseRepository
) {

    operator fun invoke(
        addResumeResponseRequest: AddResumeResponseRequest
    ) : Flow<Resource<ResumeResponseResponse>> = flow {
        try {
            val response = resumeResponseRepository.addResumeResponse(addResumeResponseRequest)
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