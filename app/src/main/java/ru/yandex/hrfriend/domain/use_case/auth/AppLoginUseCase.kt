package ru.yandex.hrfriend.domain.use_case.auth

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.yandex.hrfriend.data.dto.ErrorResponse
import ru.yandex.hrfriend.data.dto.sign_in_up.InvalidLoginRequestException
import ru.yandex.hrfriend.data.dto.sign_in_up.LoginRequest
import ru.yandex.hrfriend.data.dto.sign_in_up.LoginResponse
import ru.yandex.hrfriend.domain.repository.AuthRepository
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import ru.yandex.hrfriend.util.Resource
import javax.inject.Inject

class AppLoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(
        loginRequest: LoginRequest
    ) : Flow<Resource<LoginResponse>>  = flow {
        try {
            if (loginRequest.email.isBlank()) {
                throw InvalidLoginRequestException("Поле Email не может быть пустым !")
            }
            if (loginRequest.password.isBlank()) {
                throw InvalidLoginRequestException("Поле Password не может быть пустым !")
            }
            val response = repository.breslerAppLogin(loginRequest)
            val result = response.body()
            if (response.isSuccessful && result != null) {

                preferencesManager.putString(Constants.JWT_KEY, result.access_token)
                preferencesManager.putLong(Constants.TIMESTAMP, System.currentTimeMillis())
                preferencesManager.putString(Constants.USERNAME, result.firstname )
                preferencesManager.putString(Constants.LASTNAME, result.lastname )
                preferencesManager.putString(Constants.ROLE, result.role )
                preferencesManager.putString(Constants.PATH_LINK, result.resumePath )
                preferencesManager.putString(Constants.EMAIL, result.email )
                preferencesManager.putString(Constants.JWT_REFRESH, result.refresh_token)

                emit(Resource.Success(result))
            } else {
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