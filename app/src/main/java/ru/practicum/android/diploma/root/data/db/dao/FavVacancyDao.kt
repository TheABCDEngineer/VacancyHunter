package ru.practicum.android.diploma.root.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.root.data.db.entity.FavVacancyEntity

@Dao
interface FavVacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addVacancy(vacancy: FavVacancyEntity): Long

    @Query("DELETE FROM fav_vacancies_table WHERE id=:vacancyId")
    fun deleteVacancy(vacancyId: String): Int

    @Query("SELECT EXISTS(SELECT * FROM fav_vacancies_table WHERE id = :vacancyId)")
    fun checkIfIsFavorite(vacancyId: String): Boolean

    @Query("SELECT * FROM fav_vacancies_table WHERE id = :vacancyId")
    fun getVacancyById(vacancyId: String): FavVacancyEntity

    @Query("SELECT * FROM fav_vacancies_table ORDER BY created_at DESC")
    fun getPagedFavorites(): PagingSource<Int, FavVacancyEntity>

}