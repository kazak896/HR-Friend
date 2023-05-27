package ru.yandex.hrfriend.presentation.main.vacancy.events

import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyType

sealed class AddVacancyTypeEvent {
    class Success(val result: VacancyType?): AddVacancyTypeEvent()
    class Failure(val errorText: String?): AddVacancyTypeEvent()
    object Loading: AddVacancyTypeEvent()
    object Empty: AddVacancyTypeEvent()
}
