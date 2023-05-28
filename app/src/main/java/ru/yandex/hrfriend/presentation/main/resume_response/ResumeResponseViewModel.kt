package ru.yandex.hrfriend.presentation.main.resume_response

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.yandex.hrfriend.data.dto.resume_response.AddResumeResponseRequest
import ru.yandex.hrfriend.domain.use_case.resume_response.ResumeResponseUseCases
import ru.yandex.hrfriend.presentation.main.resume_response.events.AddResumeResponseEvent
import ru.yandex.hrfriend.presentation.main.resume_response.events.ResumeResponseEvent
import ru.yandex.hrfriend.presentation.main.vacancy.events.GetVacancyTypesEvent
import ru.yandex.hrfriend.util.Resource
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ResumeResponseViewModel  @Inject constructor(
    private val resumeResponseUseCases: ResumeResponseUseCases
) : ViewModel() {

    private val _saveFlow = MutableSharedFlow<AddResumeResponseEvent>()
    val saveFlow = _saveFlow.asSharedFlow()

    private val _getByUserFlow = MutableStateFlow<ResumeResponseEvent>(ResumeResponseEvent.Empty)
    val getByUserFlow = _getByUserFlow.asStateFlow()

    fun saveResumeResponse(
        vacancyId: String,
        candidateResume: String
    ) {
        Log.d("TAG", vacancyId.toString())
        resumeResponseUseCases.addResumeResponseUseCase(
           AddResumeResponseRequest(candidateResume, vacancyId)
        ).onEach {
            when (it) {
                is Resource.Error -> {
                    _saveFlow.emit(AddResumeResponseEvent.Failure(it.message))
                }
                is Resource.Success -> {
                    _saveFlow.emit(AddResumeResponseEvent.Success(it.data))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getByUser() {
        resumeResponseUseCases.getByUserUseCase().onEach {
            when (it) {
                is Resource.Error -> {
                    _getByUserFlow.emit(ResumeResponseEvent.Failure(it.message))
                }
                is Resource.Success -> {
                    _getByUserFlow.emit(ResumeResponseEvent.Success(it.data))
                }
            }
        }.launchIn(viewModelScope)
    }

}