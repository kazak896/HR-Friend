package ru.yandex.hrfriend.data.dto.vacancy

data class ResumeResponse(
    val candidate: Candidate,
    val candidateResumePath: String,
    val id: String,
    val status: String,
    val time: String,
    val vacancy: String
)