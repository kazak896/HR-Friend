package ru.yandex.hrfriend.data.dto.vacancy_type

data class VacancyTypeDto(
    val position: String
) {
    override fun toString(): String {
        return "VacancyTypeDto(position='$position')"
    }
}
