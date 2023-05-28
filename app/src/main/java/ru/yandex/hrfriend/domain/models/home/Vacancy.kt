package ru.yandex.hrfriend.domain.models.home

import ru.yandex.hrfriend.data.dto.vacancy.Position
import java.util.UUID

data class Vacancy (
    val id : String,
    val position : Position,
    val replacementDate: String,
    val location : String,
    val salary : String,
    val startYearsXP: Int,
    val description: String,
    val endYearsXP: Int,
)