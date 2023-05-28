package ru.yandex.hrfriend.domain.use_case.resume_response

data class ResumeResponseUseCases(
    val addResumeResponseUseCase: AddResumeResponseUseCase,
    val deleteResumeResponseUseCase: DeleteResumeResponseUseCase,
    val getByUserUseCase: GetByUserUseCase,
    val getByVacancyIdUseCase: GetByVacancyIdUseCase,
    val updateStatusResumeResponseUseCase: UpdateStatusResumeResponseUseCase
)
