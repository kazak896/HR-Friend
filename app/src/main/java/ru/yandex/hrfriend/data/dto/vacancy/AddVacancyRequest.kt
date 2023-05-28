package ru.yandex.hrfriend.data.dto.vacancy

data class AddVacancyRequest(
    val description: String,
    val endYearsXP: Int,
    val location: String,
    val position: Position,
    val salary: String,
    val startYearsXP: Int
) {
    override fun toString(): String {
        return "AddVacancyRequest(desciption='$description', endYearsXP=$endYearsXP, location='$location', position=$position, salary='$salary', startYearsXP=$startYearsXP)"
    }
}