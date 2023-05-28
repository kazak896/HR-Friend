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
) {
    override fun toString(): String {
        return "Content(desciption='$desciption', endYearsXP=$endYearsXP, id='$id', location='$location', position=$position, replacementDate='$replacementDate', salary='$salary', startYearsXP=$startYearsXP, resumeResponses=$resumeResponses)"
    }
}