package ru.practicum.android.diploma.root.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_vacancies_table")
data class FavVacancyEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val vacancyId: String,

    @ColumnInfo(name = "vacancy_name")
    val vacancyName: String,

    @ColumnInfo(name = "salary")
    val salary: String,

    @ColumnInfo(name = "logo_url")
    val logoUrl: String,

    @ColumnInfo(name = "employer_name")
    val employerName: String,

    @ColumnInfo(name = "employer_area")
    val employerArea: String,

    @ColumnInfo(name = "experience_req")
    val experienceReq: String,

    @ColumnInfo(name = "employment_type")
    val employmentType: String,

    @ColumnInfo(name = "schedule_type")
    val scheduleType: String,

    @ColumnInfo(name = "vacancy_description")
    val vacancyDescription: String,

    @ColumnInfo(name = "key_skills")
    val keySkills: String,

    @ColumnInfo(name = "contacts_name")
    val contactsName: String,

    @ColumnInfo(name = "contacts_email")
    val contactsEmail: String,

    @ColumnInfo(name = "contacts_phones")
    val contactsPhones: String,

    @ColumnInfo(name = "share_vacancy_url")
    val shareVacancyUrl: String,

    @ColumnInfo(name = "employer_address")
    val employerAddress: String,

    @ColumnInfo(name = "created_at")
    val createdTimeStamp: Long
)