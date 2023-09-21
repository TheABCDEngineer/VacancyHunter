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
            val pagedData = foundFavorites.cachedIn(viewModelScope)
            pagedData.collect { pagingData ->
                val pagedVacancies = pagingData.map {domainModel ->
                    uiMapper(domainModel)
                }
                _state.postValue(FavoritesScreenState.ContentPaged(pagedVacancies))
            }
        }
    }
}