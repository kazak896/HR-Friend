package ru.yandex.hrfriend.data.dto.vacancy_type

import ru.yandex.hrfriend.data.dto.vacancy.Position
import java.util.UUID

data class VacancyType(
    val id: UUID,
    val position: String
) {
    override fun toString(): String {
        return "VacancyType(id=$id, position='$position')"
    }
}
fun VacancyType.toPosition(): Position {
    return Position(id, position)
}