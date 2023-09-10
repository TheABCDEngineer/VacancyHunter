package ru.practicum.android.diploma.features.favorites.domain

import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails

interface FavoritesInteractor {

    suspend fun addToFavourites(vacancy: VacancyDetails): Boolean

    suspend fun deleteFromFavorites(vacancyId: String): Boolean
}