package ru.yandex.hrfriend.data.dto.vacancy

data class AddVacancyRequest(
    val desciption: String,
    val endYearsXP: Int,
    val location: String,
    val position: Position,
    val salary: String,
    val startYearsXP: Int
)