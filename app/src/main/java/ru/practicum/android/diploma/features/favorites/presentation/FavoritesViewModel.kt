package ru.practicum.android.diploma.features.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.features.favorites.domain.FavoritesInteractor
import ru.practicum.android.diploma.features.favorites.presentation.models.FavoritesScreenState
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiMapper
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.domain.model.Outcome

class FavoritesViewModel(
    private val interactor: FavoritesInteractor,
    private val uiMapper: VacancyShortUiMapper
) : ViewModel() {

    private val _state = MutableLiveData<FavoritesScreenState>()
    val state: LiveData<FavoritesScreenState> get() = _state

    fun getPagedFavorites() {
        viewModelScope.launch {
            _state.postValue(FavoritesScreenState.Loading)

            val foundFavorites = interactor.getPagedFavorites()

            when(foundFavorites) {
                is Outcome.Error -> {
                    if ((foundFavorites.data == null) &&
                        (foundFavorites.status == NetworkResultCode.CONNECTION_ERROR)) {
                            _state.postValue(FavoritesScreenState.Error(NetworkResultCode.CONNECTION_ERROR))
                    } else if ((foundFavorites.data == null)) {
                            _state.postValue(FavoritesScreenState.Error(null))
                    }
                }

                is Outcome.Success -> {
                    val data = foundFavorites.data
                    if (data == null) {
                        _state.postValue(FavoritesScreenState.Error(null))
                    } else {
                        val pagedData = data.cachedIn(viewModelScope)
                        pagedData.collect { pagingData ->
                            val pagedVacancies = pagingData.map {domainModel ->
                                uiMapper(domainModel)
                            }
                            _state.postValue(FavoritesScreenState.ContentPaged(pagedVacancies))
                        }
                    }
                }
            }
        }
    }
}