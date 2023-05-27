package ru.yandex.hrfriend.domain.use_case.vacancy_type

data class VacancyTypeUseCases(
    val getVacanciesTypesUseCase: GetVacanciesTypesUseCase,
    val addVacancyTypeUseCase: AddVacancyTypeUseCase,
    val deleteVacancyTypeUseCase: DeleteVacancyTypeUseCase
)