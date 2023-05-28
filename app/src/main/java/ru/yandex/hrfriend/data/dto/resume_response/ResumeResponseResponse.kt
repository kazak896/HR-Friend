package ru.yandex.hrfriend.data.dto.resume_response

data class ResumeResponseResponse(
    val candidate: Candidate,
    val candidateResumePath: String,
    val id: String,
    val status: String,
    val time: String,
    val vacancy: Vacancy
)