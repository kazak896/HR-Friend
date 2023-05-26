package ru.yandex.hrfriend.data.dto.vacancy

data class Content(
    val desciption: String,
    val endYearsXP: Int,
    val id: String,
    val location: String,
    val position: Position,
    val replacementDate: String,
    val salary: String,
    val startYearsXP: Int,
    val resumeResponses: List<ResumeResponse>
)