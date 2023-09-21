package ru.practicum.android.diploma.features.favorites.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.root.domain.model.VacancyShortDomainModel
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails

interface FavoritesInteractor {

    suspend fun toggleFavorites(vacancy: VacancyDetails): Boolean

    suspend fun addToFavorites(vacancy: VacancyDetails): Boolean

    suspend fun deleteFromFavorites(vacancyId: String): Boolean

    suspend fun getPagedFavorites(): Flow<PagingData<VacancyShortDomainModel>>
}