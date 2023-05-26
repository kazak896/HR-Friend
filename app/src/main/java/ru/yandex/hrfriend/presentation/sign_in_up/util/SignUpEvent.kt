package ru.yandex.hrfriend.presentation.sign_in_up.util

import ru.yandex.hrfriend.data.dto.sign_in_up.SignUpResponse

sealed class SignUpEvent {
    class Success(val result: SignUpResponse?) : SignUpEvent()
    class Failure(val errorText: String?) : SignUpEvent()
    object Loading : SignUpEvent()
}

