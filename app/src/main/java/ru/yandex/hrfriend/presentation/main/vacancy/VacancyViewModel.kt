package ru.yandex.hrfriend.presentation.main.vacancy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.Position
import ru.yandex.hrfriend.data.dto.vacancy.VacancyRequest
import ru.yandex.hrfriend.domain.use_case.vacancy.VacancyUseCases
import ru.yandex.hrfriend.presentation.main.vacancy.events.AddVacancyEvent
import ru.yandex.hrfriend.presentation.main.vacancy.events.GetVacanciesEvent
import ru.yandex.hrfriend.util.Resource
import javax.inject.Inject

@HiltViewModel
class VacancyViewModel @Inject constructor(
    private val vacancyUseCases: VacancyUseCases
) : ViewModel() {

    private val _saveFlow = MutableSharedFlow<AddVacancyEvent>()
    val saveFlow = _saveFlow.asSharedFlow()

    private val _getAllFlow = MutableStateFlow<GetVacanciesEvent>(GetVacanciesEvent.Empty)
    val getAllFlow = _getAllFlow.asStateFlow()

    fun saveVacancy(
         desciption: String,
         endYearsXP: Int,
         location: String,
         position: Position,
         salary: String,
         startYearsXP: Int
    ) {
        vacancyUseCases.addVacancyUseCase(AddVacancyRequest(
            desciption,
            endYearsXP,
            location, position,
            salary,
            startYearsXP
        )).onEach {
            when (it) {
                is Resource.Error -> {
                    _saveFlow.emit(AddVacancyEvent.Failure(it.message))
                }
                is Resource.Success -> {
                    _saveFlow.emit(AddVacancyEvent.Success(it.data))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getVacancies(
        vacancyRequest: VacancyRequest
    ) {
        vacancyUseCases.getVacanciesUseCase(vacancyRequest).onEach {
            when (it) {
                is Resource.Error -> {
                    _getAllFlow.emit(GetVacanciesEvent.Failure(it.message))
                }
                is Resource.Success -> {
                    _getAllFlow.emit(GetVacanciesEvent.Success(it.data))
                }
            }
        }
    }


}