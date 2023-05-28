package ru.yandex.hrfriend.presentation.main.vacancy.events

import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyResponse

sealed class AddVacancyEvent {
    class Success(val result: AddVacancyResponse?): AddVacancyEvent()
    class Failure(val errorText: String?): AddVacancyEvent()
    object Loading: AddVacancyEvent()
    object Empty: AddVacancyEvent()
}
