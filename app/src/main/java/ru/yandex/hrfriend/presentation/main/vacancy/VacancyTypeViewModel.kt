package ru.yandex.hrfriend.presentation.main.vacancy

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
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyTypeDto
import ru.yandex.hrfriend.domain.use_case.vacancy_type.VacancyTypeUseCases
import ru.yandex.hrfriend.presentation.main.vacancy.events.AddVacancyTypeEvent
import ru.yandex.hrfriend.presentation.main.vacancy.events.GetVacancyTypesEvent
import ru.yandex.hrfriend.util.Resource
import javax.inject.Inject

@HiltViewModel
class VacancyTypeViewModel @Inject constructor(
    private val vacancyTypeUseCases: VacancyTypeUseCases
) : ViewModel() {

    private val _saveFlow = MutableSharedFlow<AddVacancyTypeEvent>()
    val saveFlow = _saveFlow.asSharedFlow()

    private val _getAllFlow = MutableStateFlow<GetVacancyTypesEvent>(GetVacancyTypesEvent.Empty)
    val getAllFlow = _getAllFlow.asStateFlow()

    fun getVacancyTypes() {
        vacancyTypeUseCases.getVacanciesTypesUseCase().onEach {
            when (it) {
                is Resource.Error -> {
                    _getAllFlow.emit(GetVacancyTypesEvent.Failure(it.message))
                }
                is Resource.Success -> {
                    _getAllFlow.emit(GetVacancyTypesEvent.Success(it.data))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun saveVacancyType(
        position: String
    ) {
        Log.d("TAG", position)
        vacancyTypeUseCases.addVacancyTypeUseCase(
            VacancyTypeDto(position)
        ).onEach {
            when (it) {
                is Resource.Error -> {
                    _saveFlow.emit(AddVacancyTypeEvent.Failure(it.message))
                }
                is Resource.Success -> {
                    _saveFlow.emit(AddVacancyTypeEvent.Success(it.data))
                }
            }
        }.launchIn(viewModelScope)
    }

}