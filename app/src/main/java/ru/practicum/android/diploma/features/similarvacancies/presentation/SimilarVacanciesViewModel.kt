package ru.practicum.android.diploma.features.similarvacancies.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.features.similarvacancies.domain.SimilarVacanciesInteractor
import ru.practicum.android.diploma.features.similarvacancies.presentation.models.SimilarVacanciesState
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode

class SimilarVacanciesViewModel(
    private val interactor: SimilarVacanciesInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SimilarVacanciesState>()
    val state: LiveData<SimilarVacanciesState> get() = _state

    fun getSimilarVacancies(vacancyId: String) {
        viewModelScope.launch {
            _state.postValue(SimilarVacanciesState.Loading)

            interactor.getSimilarVacancies(vacancyId).collect {
                when {
                    (it.data == null) && (it.status == NetworkResultCode.CONNECTION_ERROR) -> {
                        _state.postValue(SimilarVacanciesState.Error(NetworkResultCode.CONNECTION_ERROR))
                    }

                    (it.data == null) -> _state.postValue(SimilarVacanciesState.Error(null))

                    it.data.isEmpty() -> _state.postValue(SimilarVacanciesState.NothingFound)

                    else -> _state.postValue(SimilarVacanciesState.Content(it.data))
                }
            }
        }
    }
}