package ru.practicum.android.diploma.features.favorites.presentation.models

import androidx.paging.PagingData
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiModel
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode

sealed class FavoritesScreenState {

    object Loading : FavoritesScreenState()

    object NothingFound : FavoritesScreenState()

    data class Error(
        val errorCode: NetworkResultCode?
    ) : FavoritesScreenState()

    data class Content(
        val favoriteVacancies: List<VacancyShortUiModel>,
    ) : FavoritesScreenState()

    data class ContentPaged(
        val favoriteVacancies: PagingData<VacancyShortUiModel>,
    ) : FavoritesScreenState()

}