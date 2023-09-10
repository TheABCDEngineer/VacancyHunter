package ru.practicum.android.diploma.root.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.root.data.db.dao.FavVacancyDao
import ru.practicum.android.diploma.root.data.db.entity.FavVacancyEntity

@Database(version = 1, entities = [FavVacancyEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favVacancyDao(): FavVacancyDao

}