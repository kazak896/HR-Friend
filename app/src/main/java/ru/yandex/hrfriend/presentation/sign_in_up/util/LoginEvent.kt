package ru.yandex.hrfriend.presentation.sign_in_up.util

import ru.yandex.hrfriend.data.dto.sign_in_up.LoginResponse

sealed class LoginEvent {
    class Success(val result: LoginResponse?): LoginEvent()
    class Failure(val errorText: String?): LoginEvent()
    object Loading: LoginEvent()
    object Error: LoginEvent()
}
