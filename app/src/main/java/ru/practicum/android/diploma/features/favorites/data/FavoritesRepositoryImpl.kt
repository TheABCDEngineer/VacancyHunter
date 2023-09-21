package ru.practicum.android.diploma.features.favorites.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.features.favorites.domain.FavoritesRepository
import ru.practicum.android.diploma.root.domain.model.VacancyShortDomainModel
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.data.VacancyDbConverter
import ru.practicum.android.diploma.root.data.db.AppDatabase
import ru.practicum.android.diploma.root.data.db.entity.FavVacancyEntity

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val vacancyDbConverter: VacancyDbConverter
): FavoritesRepository {
    override suspend fun addToFavorites(vacancy: VacancyDetails): Boolean {
        val result = appDatabase.favVacancyDao().addVacancy(convertToVacancyEntity(vacancy))
        return (result != CODE_WHEN_INSERT_FAILED)
    }

    override suspend fun deleteFromFavorites(vacancyId: String): Boolean {
        val result = appDatabase.favVacancyDao().deleteVacancy(vacancyId)
        return (result != NUMBER_OF_LINES_WHEN_OPERATION_FAILED)
    }

    override suspend fun checkIfIsFavorite(vacancyId: String): Boolean {
        return appDatabase.favVacancyDao().checkIfIsFavorite(vacancyId)
    }

    override suspend fun getVacancyById(vacancyId: String): VacancyDetails {
        val favVacancyEntity = appDatabase.favVacancyDao().getVacancyById(vacancyId)
        return vacancyDbConverter.mapVacancyEntityToVacancyDetails(favVacancyEntity)
    }

    override suspend fun getPagedFavorites(): Flow<PagingData<VacancyShortDomainModel>> {
        val flowPagedEntities = Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)
        ){
            appDatabase.favVacancyDao().getPagedFavorites()
        }.flow
        val flowPagedVacancies = flowPagedEntities.map {pagingData ->
            pagingData.map {
                vacancyDbConverter.mapVacancyEntityToVacancyShort(it)
            }
        }
        return flowPagedVacancies
    }

    private fun convertToVacancyEntity(vacancy: VacancyDetails): FavVacancyEntity {
        return vacancyDbConverter.mapVacancyDetailsToVacancyEntity(vacancy)
    }

    companion object {
        private const val NUMBER_OF_LINES_WHEN_OPERATION_FAILED = 0
        private const val CODE_WHEN_INSERT_FAILED = -1L

        private const val PAGE_SIZE = 20
    }

}