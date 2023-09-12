package ru.practicum.android.diploma.features.favorites.presentation.models

import ru.practicum.android.diploma.features.similarvacancies.presentation.models.VacancySimilarShortUiModel
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode

sealed class FavoritesScreenState {

    object Loading : FavoritesScreenState()

    object NothingFound : FavoritesScreenState()

    data class Error(
        val errorCode: NetworkResultCode?
    ) : FavoritesScreenState()

    data class Content(
        val favoriteVacancies: List<VacancySimilarShortUiModel>,
    ) : FavoritesScreenState()

}