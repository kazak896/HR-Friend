package ru.yandex.hrfriend.data.dto.resume_response

data class AddResumeResponseRequest(
    val candidateResume: String,
    val vacancyId: String
)