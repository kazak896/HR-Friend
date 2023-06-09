package ru.yandex.hrfriend.domain.use_case.auth

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.yandex.hrfriend.data.dto.ErrorResponse
import ru.yandex.hrfriend.data.dto.sign_in_up.InvalidLoginRequestException
import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpRequest
import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpResponse
import ru.yandex.hrfriend.domain.repository.AuthRepository
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import ru.yandex.hrfriend.util.Resource
import java.lang.Exception
import javax.inject.Inject
import kotlin.math.log

class AppSignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(
        signUpRequest: SignUpRequest
    ) : Flow<Resource<SignUpResponse>> = flow {
        try {
            if (signUpRequest.firstname.isBlank()) {
                throw InvalidLoginRequestException("Поле Name не может быть пустым !")
            }
            if (signUpRequest.email.isBlank()) {
                throw InvalidLoginRequestException("Поле Email не может быть пустым !")
            }
            if (signUpRequest.password.isBlank()) {
                throw InvalidLoginRequestException("Поле Password не может быть пустым !")
            }
            val response = authRepository.breslerAppSignUp(signUpRequest)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Log.d("TAG", result.toString())
                preferencesManager.putString(Constants.JWT_KEY, result.access_token)
                preferencesManager.putLong(Constants.TIMESTAMP, System.currentTimeMillis())
                preferencesManager.putString(Constants.USERNAME, result.firstname )
                preferencesManager.putString(Constants.LASTNAME, result.lastname )
                preferencesManager.putString(Constants.ROLE, result.role )
                preferencesManager.putString(Constants.EMAIL, result.email )
                preferencesManager.putString(Constants.PATH_LINK, result.resumePath )
                preferencesManager.putString(Constants.JWT_REFRESH, result.refresh_token)
                emit(Resource.Success(result))
            } else {
                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type
                val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                if (errorResponse != null) {
                    emit(Resource.Error(errorResponse.description))
                } else {
                    emit(Resource.Error("Error..."))
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }

    }
}