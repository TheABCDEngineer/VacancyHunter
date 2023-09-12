package ru.practicum.android.diploma.features.favorites.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {

    override suspend fun toggleFavorites(vacancy: VacancyDetails): Boolean {
        return if (vacancy.isFavorite) {
            deleteFromFavorites(vacancy.vacancyId)
        } else {
            addToFavorites(vacancy)
        }
    }

    override suspend fun addToFavorites(vacancy: VacancyDetails): Boolean {
        return withContext(Dispatchers.IO) {
            favoritesRepository.addToFavorites(vacancy)
        }
    }

    override suspend fun deleteFromFavorites(vacancyId: String): Boolean {
        return withContext(Dispatchers.IO) {
            favoritesRepository.deleteFromFavorites(vacancyId)
        }
    }

}