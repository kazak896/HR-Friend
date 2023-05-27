package ru.yandex.hrfriend.domain.use_case.vacancy_type

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.yandex.hrfriend.data.dto.ErrorResponse
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyType
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyTypeDto
import ru.yandex.hrfriend.domain.repository.VacancyTypeRepository
import ru.yandex.hrfriend.util.Resource
import javax.inject.Inject

class GetVacanciesTypesUseCase @Inject constructor(
    private val vacancyTypeRepository: VacancyTypeRepository
) {
    operator fun invoke() : Flow<Resource<List<VacancyType>>> = flow {
        try {
            val response = vacancyTypeRepository.getAll()
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