package ru.practicum.android.diploma.features.favorites.data

import ru.practicum.android.diploma.features.favorites.domain.FavoritesRepository
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.data.VacancyDbConverter
import ru.practicum.android.diploma.root.data.db.AppDatabase
import ru.practicum.android.diploma.root.data.db.entity.FavVacancyEntity
import ru.practicum.android.diploma.root.domain.model.Outcome

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
        return vacancyDbConverter.mapToVacancyDetails(favVacancyEntity)
    }

    override suspend fun getFavoriteVacancies(): Outcome<List<VacancyDetails>> {
        val foundFavoriteVacancies = appDatabase.favVacancyDao().getFavoriteVacancies()
        val listOfVacancies = foundFavoriteVacancies.map {
            vacancyDbConverter.mapToVacancyDetails(it)
        }
        return Outcome.Success(data = listOfVacancies)
    }

    private fun convertToVacancyEntity(vacancy: VacancyDetails): FavVacancyEntity {
        return vacancyDbConverter.mapToVacancyEntity(vacancy)
    }

    companion object {
        private const val NUMBER_OF_LINES_WHEN_OPERATION_FAILED = 0
        private const val CODE_WHEN_INSERT_FAILED = -1L
    }

}