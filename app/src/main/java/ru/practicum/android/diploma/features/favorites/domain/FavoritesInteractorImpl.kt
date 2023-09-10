package ru.practicum.android.diploma.features.favorites.domain

import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {

    override suspend fun addToFavourites(vacancy: VacancyDetails): Boolean {
        val result = favoritesRepository.addToFavorites(vacancy)
        return result
    }

    override suspend fun deleteFromFavorites(vacancyId: String): Boolean {
        val result = favoritesRepository.deleteFromFavorites(vacancyId)
        return result
    }

}