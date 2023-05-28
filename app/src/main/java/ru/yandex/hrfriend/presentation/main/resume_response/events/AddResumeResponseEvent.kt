package ru.yandex.hrfriend.presentation.main.resume_response.events

import ru.yandex.hrfriend.data.dto.resume_response.ResumeResponseResponse

sealed class AddResumeResponseEvent {
    class Success(val result: ResumeResponseResponse?): AddResumeResponseEvent()
    class Failure(val errorText: String?): AddResumeResponseEvent()
    object Loading: AddResumeResponseEvent()
    object Empty: AddResumeResponseEvent()
}
