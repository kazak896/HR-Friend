package ru.yandex.hrfriend.domain.use_case.vacancy

data class VacancyUseCases(
    val getVacanciesUseCase: GetVacanciesUseCase,
    val addVacancyUseCase: AddVacancyUseCase,
    val deleteVacancyUseCase: DeleteVacancyUseCase,
    val updateVacancyUseCase: UpdateVacancyUseCase,
    val getVacancyByIdUseCase: GetVacancyByIdUseCase
)