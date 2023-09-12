package ru.practicum.android.diploma.features.vacancydetails.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.features.favorites.domain.FavoritesRepository
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.domain.VacancyRepository
import ru.practicum.android.diploma.root.domain.model.Outcome


class VacancyDetailsInteractorImpl(
    private val vacancyRepository: VacancyRepository,
    private val favoritesRepository: FavoritesRepository
) : VacancyDetailsInteractor {

    override suspend fun getVacancyById(id: String): Outcome<VacancyDetails> {
        val outcome = searchVacancyById(id)

        val isFavorite = checkIfIsFavorite(id)
        if (!isFavorite) return outcome

        when (outcome) {
            is Outcome.Error -> {
                return fetchFromFavoritesDb(id)
            }

            is Outcome.Success -> {
                val vacancyDetails = outcome.data
                if (vacancyDetails == null) return fetchFromFavoritesDb(id)
                val vacancyMarkedFavorite = vacancyDetails.copy(isFavorite = true)
                return Outcome.Success(vacancyMarkedFavorite)
            }
        }
    }

    private suspend fun searchVacancyById(id: String): Outcome<VacancyDetails> {
        return withContext(Dispatchers.IO) {
            vacancyRepository.getVacancyById(id)
        }
    }

    private suspend fun checkIfIsFavorite(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            favoritesRepository.checkIfIsFavorite(id)
        }
    }

    private suspend fun fetchFromFavoritesDb(id: String): Outcome<VacancyDetails> {
        val vacancyFromFavorites = withContext(Dispatchers.IO) {
            favoritesRepository.getVacancyById(id)
        }
        return Outcome.Success(data = vacancyFromFavorites)
    }

}