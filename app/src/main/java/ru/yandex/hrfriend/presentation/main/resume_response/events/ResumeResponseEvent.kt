package ru.yandex.hrfriend.presentation.main.resume_response.events

import ru.yandex.hrfriend.data.dto.resume_response.ResumeResponseResponse

sealed class ResumeResponseEvent {
    class Success(val result: List<ResumeResponseResponse>?): ResumeResponseEvent()
    class Failure(val errorText: String?): ResumeResponseEvent()
    object Loading: ResumeResponseEvent()
    object Empty: ResumeResponseEvent()
}
