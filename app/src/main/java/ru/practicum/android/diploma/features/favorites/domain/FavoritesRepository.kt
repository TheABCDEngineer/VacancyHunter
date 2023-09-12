package ru.practicum.android.diploma.features.favorites.domain

import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.domain.model.Outcome

interface FavoritesRepository {

    suspend fun addToFavorites(vacancy: VacancyDetails): Boolean

    suspend fun deleteFromFavorites(vacancyId: String): Boolean

    suspend fun checkIfIsFavorite(vacancyId: String): Boolean

    suspend fun getVacancyById(vacancyId: String): VacancyDetails

    suspend fun getFavoriteVacancies(): Outcome<List<VacancyShortSimilar>>

}