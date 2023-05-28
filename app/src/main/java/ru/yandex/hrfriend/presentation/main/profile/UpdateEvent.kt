package ru.yandex.hrfriend.presentation.main.profile

import ru.yandex.hrfriend.data.dto.resume_response.ResumeResponseResponse
import ru.yandex.hrfriend.presentation.main.resume_response.events.AddResumeResponseEvent

sealed class UpdateEvent {
    class Success(val result: Unit): UpdateEvent()
    class Failure(val errorText: String?): UpdateEvent()
    object Loading: UpdateEvent()
    object Empty: UpdateEvent()
}