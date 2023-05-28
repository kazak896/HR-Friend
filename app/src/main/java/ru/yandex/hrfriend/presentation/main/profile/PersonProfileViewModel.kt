package ru.yandex.hrfriend.presentation.main.profile

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
import ru.yandex.hrfriend.domain.use_case.person.PersonUseCases
import ru.yandex.hrfriend.domain.use_case.resume_response.ResumeResponseUseCases
import ru.yandex.hrfriend.presentation.main.resume_response.events.AddResumeResponseEvent
import ru.yandex.hrfriend.presentation.main.resume_response.events.ResumeResponseEvent
import ru.yandex.hrfriend.util.Resource
import javax.inject.Inject

@HiltViewModel
class PersonProfileViewModel @Inject constructor(
    private val personUseCases: PersonUseCases
) : ViewModel() {

    private val _saveFlow = MutableSharedFlow<UpdateEvent>()
    val saveFlow = _saveFlow.asSharedFlow()

    fun saveResumeLink(
        resumePath: String
    ) {
        personUseCases.updatePersonLinkUseCase(resumePath).onEach {
            when (it) {
                is Resource.Error -> {
                    _saveFlow.emit(UpdateEvent.Failure(it.message))
                }
                is Resource.Success -> {
                    _saveFlow.emit(UpdateEvent.Success(Unit))
                }
            }
        }.launchIn(viewModelScope)
    }

}