package ru.yandex.hrfriend.data.dto.resume_response

data class Vacancy(
    val desciption: String,
    val endYearsXP: Int,
    val id: String,
    val location: String,
    val position: Position,
    val replacementDate: String,
    val resumeResponses: List<String>,
    val salary: String,
    val startYearsXP: Int
)