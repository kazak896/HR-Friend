package ru.yandex.hrfriend.data.dto.resume_response

import java.util.UUID

data class AddResumeResponseRequest(
    val candidateResume: String,
    val vacancyId: String
)