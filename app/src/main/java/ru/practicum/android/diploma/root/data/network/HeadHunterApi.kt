package ru.practicum.android.diploma.root.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.features.search.data.network.dto.VacancyResponse
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto

interface HeadHunterApi {
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyById(@Path("vacancy_id") vacancyId: String): VacancyDetailsDto

//    @GET("/vacancies?{params}")
//    suspend fun getVacancyListByParameters(@Path("params") parameters: String): VacancyResponse

    @GET("/vacancies")
    suspend fun getVacancyListByParameters(@Query("text") text: String): VacancyResponse

}