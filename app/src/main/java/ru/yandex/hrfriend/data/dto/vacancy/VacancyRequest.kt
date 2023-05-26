package ru.yandex.hrfriend.data.dto.vacancy

data class VacancyRequest(
    val page: Int,
    val size: Int,
    val sort: List<String>
)