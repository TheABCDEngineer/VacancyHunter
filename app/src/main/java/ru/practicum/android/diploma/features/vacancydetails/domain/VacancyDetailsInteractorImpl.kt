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
        val outcome = vacancyRepository.getVacancyById(id)

        when (outcome) {
            is Outcome.Error -> return outcome
            is Outcome.Success -> {
                val vacancyDetails = outcome.data ?: return outcome
                val isFavorite = withContext(Dispatchers.IO) {
                    favoritesRepository.checkIfIsFavorite(id)
                }
                return if (isFavorite) {
                    val vacancyMarkedFavorite = vacancyDetails.copy(isFavorite = true)
                    Outcome.Success(vacancyMarkedFavorite)
                } else {
                    outcome
                }
            }
        }
    }

}