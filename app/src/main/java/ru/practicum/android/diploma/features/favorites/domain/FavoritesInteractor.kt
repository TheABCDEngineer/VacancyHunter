package ru.practicum.android.diploma.features.favorites.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.root.domain.model.VacancyShortDomainModel
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.domain.model.Outcome

interface FavoritesInteractor {

    suspend fun toggleFavorites(vacancy: VacancyDetails): Boolean

    suspend fun addToFavorites(vacancy: VacancyDetails): Boolean

    suspend fun deleteFromFavorites(vacancyId: String): Boolean

    suspend fun getFavoriteVacancies(): Outcome<List<VacancyShortDomainModel>>

    suspend fun getPagedFavorites(): Outcome<Flow<PagingData<VacancyShortDomainModel>>>
}