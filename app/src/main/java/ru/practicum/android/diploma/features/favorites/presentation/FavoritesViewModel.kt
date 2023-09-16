package ru.practicum.android.diploma.features.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.features.favorites.domain.FavoritesInteractor
import ru.practicum.android.diploma.features.favorites.presentation.models.FavoritesScreenState
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiMapper
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode

class FavoritesViewModel(
    private val interactor: FavoritesInteractor,
    private val uiMapper: VacancyShortUiMapper
) : ViewModel() {

    private val _state = MutableLiveData<FavoritesScreenState>()
    val state: LiveData<FavoritesScreenState> get() = _state

    fun getFavorites() {
        viewModelScope.launch {
            _state.postValue(FavoritesScreenState.Loading)

            val foundFavorites = interactor.getFavoriteVacancies()
            when {
                (foundFavorites.data == null) && (foundFavorites.status == NetworkResultCode.CONNECTION_ERROR) -> {
                    _state.postValue(FavoritesScreenState.Error(NetworkResultCode.CONNECTION_ERROR))
                }

                (foundFavorites.data == null) -> _state.postValue(FavoritesScreenState.Error(null))

                foundFavorites.data.isEmpty() -> _state.postValue(FavoritesScreenState.NothingFound)

                else -> _state.postValue(
                    FavoritesScreenState.Content(
                        foundFavorites.data.map { domainModel ->
                            uiMapper(domainModel)
                        }
                    )
                )
            }
        }
    }

    fun getPagedFavorites() {
        viewModelScope.launch {
            _state.postValue(FavoritesScreenState.Loading)

            val foundFavorites = interactor.getPagedFavorites()
            when {
                (foundFavorites.data == null) && (foundFavorites.status == NetworkResultCode.CONNECTION_ERROR) -> {
                    _state.postValue(FavoritesScreenState.Error(NetworkResultCode.CONNECTION_ERROR))
                }

                (foundFavorites.data == null) -> _state.postValue(FavoritesScreenState.Error(null))

//                foundFavorites.data.isEmpty() -> _state.postValue(FavoritesScreenState.NothingFound)
                else -> {
                    val flowPagedVacancies = foundFavorites.data.cachedIn(viewModelScope)
                    val flowGagedUiVacancies = flowPagedVacancies.map { pagingData ->
                        pagingData.map {domainModel ->
                            uiMapper(domainModel)
                        }
                    }
                    flowGagedUiVacancies.collect{it ->
                        _state.postValue(
                            FavoritesScreenState.ContentPaged(
                                it
                            )
                        )
                    }

                }
            }
        }
    }
}