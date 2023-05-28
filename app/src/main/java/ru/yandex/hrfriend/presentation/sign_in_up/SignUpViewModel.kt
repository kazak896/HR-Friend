package ru.yandex.hrfriend.presentation.sign_in_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.yandex.hrfriend.data.dto.sign_in_up.LoginRequest
import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpRequest
import ru.yandex.hrfriend.domain.use_case.auth.AuthUseCases
import ru.yandex.hrfriend.presentation.sign_in_up.util.LoginEvent
import ru.yandex.hrfriend.presentation.sign_in_up.util.SignUpEvent
import ru.yandex.hrfriend.util.Resource
import javax.inject.Inject
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<SignUpEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun signUp(
        firstname: String,
        lastname: String,
        email: String,
        password: String
    ) {


        authUseCases.appSignUpUseCase(
            SignUpRequest(
                firstname, lastname, email, password
            )
        ).onEach {
            when (it) {
                is Resource.Success -> {
                    _eventFlow.emit(SignUpEvent.Success(it.data))
                }
                is Resource.Error -> {
                    _eventFlow.emit(SignUpEvent.Failure(it.message))
                }
            }
        }.launchIn(viewModelScope)
    }

}