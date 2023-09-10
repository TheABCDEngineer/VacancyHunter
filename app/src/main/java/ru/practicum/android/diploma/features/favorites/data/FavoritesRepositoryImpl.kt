package ru.practicum.android.diploma.features.favorites.data

import ru.practicum.android.diploma.features.favorites.domain.FavoritesRepository
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.data.VacancyDbConverter

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val vacancyDbConverter: VacancyDbConverter
): FavoritesRepository {
    override suspend fun addToFavorites(vacancy: VacancyDetails): Boolean {
        val result = appDatabase.addToFavorites(convertToVacancyEntity(vacancy))
        return if (result == NUMBER_OF_LINES_WHEN_OPERATION_FAILED) false else true
    }

    override suspend fun deleteFromFavorites(vacancyId: String): Boolean {
        val result = appDatabase.deleteFromFavorites(vacancyId)
        return if (result == NUMBER_OF_LINES_WHEN_OPERATION_FAILED) false else true
    }

    private fun convertToVacancyEntity(vacancy: VacancyDetails): VacancyEntity {
        return vacancyDbConverter.mapToVacancyEntity(vacancy)
    }

    companion object {
        private const val NUMBER_OF_LINES_WHEN_OPERATION_FAILED = 0
    }

}