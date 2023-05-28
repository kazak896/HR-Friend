package ru.yandex.hrfriend.presentation.main.vacancy.events

import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyResponse
import ru.yandex.hrfriend.data.dto.vacancy.VacancyResponse

sealed class GetVacanciesEvent {
    class Success(val result: VacancyResponse?): GetVacanciesEvent()
    class Failure(val errorText: String?): GetVacanciesEvent()
    object Loading: GetVacanciesEvent()
    object Empty: GetVacanciesEvent()
}
