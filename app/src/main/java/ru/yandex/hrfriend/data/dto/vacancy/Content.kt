package ru.yandex.hrfriend.data.dto.vacancy

data class Content(
    val description: String,
    val endYearsXP: Int,
    val id: String,
    val location: String,
    val position: Position,
    val replacementDate: String,
    val salary: String,
    val startYearsXP: Int,
    val responsesCount: Int
) {
    override fun toString(): String {
        return "Content(description='$description', endYearsXP=$endYearsXP, id='$id', location='$location', position=$position, replacementDate='$replacementDate', salary='$salary', startYearsXP=$startYearsXP, responsesCount=$responsesCount)"
    }
}