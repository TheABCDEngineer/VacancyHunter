package ru.practicum.android.diploma.root.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.features.filters.data.dto.CountryDto
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesResponse
import ru.practicum.android.diploma.features.search.data.network.dto.VacancyResponse
import ru.practicum.android.diploma.features.filters.data.dto.IndustryDto
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto

interface HeadHunterApi {
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyById(@Path("vacancy_id") vacancyId: String): VacancyDetailsDto

    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacanciesById(@Path("vacancy_id") vacancyId: String): SimilarVacanciesResponse

    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacanciesByProfRoles(
        @Path("vacancy_id") vacancyId: String,
        @Query("professional_role") role: String
        ): SimilarVacanciesResponse

    @GET("/vacancies")
    suspend fun getVacancyListByParameters(
        @Query("text") requestJob: String,
        @Query("country") countryId: String?,
        @Query("area") regionId: String?,
        @Query("industry") industryId: String?,
        @Query("salary") salary: String?,
        @Query("only_with_salary") isSalary: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): VacancyResponse
  
    @GET("/industries")
    suspend fun getIndustries(): List<IndustryDto>

    @GET("/areas/countries")
    suspend fun getCountries(): List<CountryDto>
}