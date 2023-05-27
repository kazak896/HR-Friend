package ru.yandex.hrfriend.presentation.main.vacancy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.yandex.hrfriend.data.dto.vacancy.AddVacancyRequest
import ru.yandex.hrfriend.data.dto.vacancy.Position
import ru.yandex.hrfriend.domain.use_case.vacancy.VacancyUseCases
import ru.yandex.hrfriend.util.Resource
import javax.inject.Inject

@HiltViewModel
class VacancyViewModel @Inject constructor(
    private val vacancyUseCases: VacancyUseCases
) : ViewModel() {

//    private val _eventFlow = MutableSharedFlow<LoginEvent>()
//    val eventFlow = _eventFlow.asSharedFlow()

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
                is Resource.Error -> TODO()
                is Resource.Success -> TODO()
            }
        }.launchIn(viewModelScope)
    }

}