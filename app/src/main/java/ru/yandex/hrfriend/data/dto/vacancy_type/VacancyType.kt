package ru.yandex.hrfriend.data.dto.vacancy_type

import java.util.UUID

data class VacancyType(
    val id: UUID,
    val position: String
) {
    override fun toString(): String {
        return "VacancyType(id=$id, position='$position')"
    }
}