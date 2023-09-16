package ru.practicum.android.diploma.root.data.network

import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesRequest
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesResponse
import ru.practicum.android.diploma.features.filters.data.dto.IndustryDto
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsRequest
import ru.practicum.android.diploma.root.data.network.models.Response

interface NetworkSearch {

    suspend fun getVacancyById(dto: VacancyDetailsRequest): Response<VacancyDetailsDto>

    suspend fun getSimilarVacanciesById(dto: SimilarVacanciesRequest): Response<SimilarVacanciesResponse>

    suspend fun getSimilarVacanciesByProfRoles(dto: SimilarVacanciesRequest): Response<SimilarVacanciesResponse>
  
    suspend fun getIndustries(): Response<List<IndustryDto>>

}