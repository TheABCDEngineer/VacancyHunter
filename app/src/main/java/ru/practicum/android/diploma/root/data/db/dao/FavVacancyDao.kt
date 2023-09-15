package ru.practicum.android.diploma.root.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.root.data.db.entity.FavVacancyEntity

@Dao
interface FavVacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVacancy(vacancy: FavVacancyEntity): Long

    @Query("DELETE FROM fav_vacancies_table WHERE id=:vacancyId")
    suspend fun deleteVacancy(vacancyId: String): Int

    @Query("SELECT EXISTS(SELECT * FROM fav_vacancies_table WHERE id = :vacancyId)")
    suspend fun checkIfIsFavorite(vacancyId: String): Boolean

    @Query("SELECT * FROM fav_vacancies_table WHERE id = :vacancyId")
    suspend fun getVacancyById(vacancyId: String): FavVacancyEntity

    @Query("SELECT * FROM fav_vacancies_table ORDER BY created_at DESC")
    suspend fun getFavoriteVacancies(): List<FavVacancyEntity>

}