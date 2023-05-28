package ru.yandex.hrfriend.data.dto.resume_response

import java.time.LocalDateTime

data class ResumeResponseResponse(
    val candidate: Candidate,
    val candidateResumePath: String,
    val id: String,
    val status: String,
    val time: String,
    val vacancy: Vacancy
) {
    override fun toString(): String {
        return "ResumeResponseResponse(candidate=$candidate, candidateResumePath='$candidateResumePath', id='$id', status='$status', time='$time', vacancy=$vacancy)"
    }
}