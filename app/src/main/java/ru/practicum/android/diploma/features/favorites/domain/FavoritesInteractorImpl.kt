package ru.practicum.android.diploma.features.favorites.domain

import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.root.domain.model.VacancyShortDomainModel
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.domain.model.Outcome

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {

    override suspend fun toggleFavorites(vacancy: VacancyDetails): Boolean {
        return if (vacancy.isFavorite) {
            val deleteFromFavoriteResult = deleteFromFavorites(vacancy.vacancyId)
            val isFavoriteNewStatus = !deleteFromFavoriteResult
            isFavoriteNewStatus
        } else {
            val isFavoriteNewStatus = addToFavorites(vacancy)
            isFavoriteNewStatus
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

    override suspend fun getPagedFavorites(): Outcome<Flow<PagingData<VacancyShortDomainModel>>> {
        return withContext(Dispatchers.IO) {
            favoritesRepository.getPagedFavorites()
        }
    }
}