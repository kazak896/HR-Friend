package ru.yandex.hrfriend.domain.use_case.vacancy

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.yandex.hrfriend.data.dto.ErrorResponse
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyResponse
import ru.yandex.hrfriend.domain.repository.VacancyRepository
import ru.yandex.hrfriend.util.Resource
import java.util.UUID
import javax.inject.Inject

class UpdateVacancyUseCase @Inject constructor(
    private val repository: VacancyRepository
){
    operator fun invoke(
        id: UUID,
        addVacancyRequest: AddVacancyRequest
    ) : Flow<Resource<AddVacancyResponse>> = flow {
        try {
            val response = repository.update(id, addVacancyRequest)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                emit(Resource.Success(result))
            } else {
                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type
                val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                if (errorResponse != null) {
                    emit(Resource.Error(errorResponse.description))
                }
            }
        } catch (e:Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }
}