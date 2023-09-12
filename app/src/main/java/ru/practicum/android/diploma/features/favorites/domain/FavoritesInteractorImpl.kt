package ru.practicum.android.diploma.features.favorites.domain

import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {

    override suspend fun toggleFavorites(vacancy: VacancyDetails): Boolean {
        return if (vacancy.isFavorite) {
            addToFavorites(vacancy)
        } else {
            deleteFromFavorites(vacancy.vacancyId)
        }
    }

    override suspend fun addToFavorites(vacancy: VacancyDetails): Boolean {
        return favoritesRepository.addToFavorites(vacancy)
    }

    override suspend fun deleteFromFavorites(vacancyId: String): Boolean {
        return favoritesRepository.deleteFromFavorites(vacancyId)
    }

}