package ru.practicum.android.diploma.features.favorites.domain

import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails

interface FavoritesRepository {

    suspend fun addToFavorites(vacancy: VacancyDetails): Boolean

    suspend fun deleteFromFavorites(vacancyId: String): Boolean

    suspend fun checkIfIsFavorite(vacancyId: String): Boolean

    suspend fun getVacancyById(vacancyId: String): VacancyDetails

}