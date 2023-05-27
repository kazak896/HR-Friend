package ru.yandex.hrfriend.presentation.main.vacancy.events

import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyType

sealed class GetVacancyTypesEvent {
    class Success(val result: List<VacancyType>?): GetVacancyTypesEvent()
    class Failure(val errorText: String?): GetVacancyTypesEvent()
    object Loading: GetVacancyTypesEvent()
    object Empty: GetVacancyTypesEvent()
}
