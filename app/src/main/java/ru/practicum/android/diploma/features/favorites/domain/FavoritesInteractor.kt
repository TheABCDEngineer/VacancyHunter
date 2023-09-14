package ru.practicum.android.diploma.features.favorites.domain

import ru.practicum.android.diploma.root.domain.model.VacancyShortDomainModel
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.domain.model.Outcome

interface FavoritesInteractor {

    suspend fun toggleFavorites(vacancy: VacancyDetails): Boolean

    suspend fun addToFavorites(vacancy: VacancyDetails): Boolean

    suspend fun deleteFromFavorites(vacancyId: String): Boolean

    suspend fun getFavoriteVacancies(): Outcome<List<VacancyShortDomainModel>>
}