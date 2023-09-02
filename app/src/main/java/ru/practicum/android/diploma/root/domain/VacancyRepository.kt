package ru.practicum.android.diploma.root.domain

import ru.practicum.android.diploma.features.similarvacancies.domain.models.SimilarityParams
import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.data.Outcome

interface VacancyRepository {
    suspend fun getVacancyById(id: String): Outcome<VacancyDetails>

    suspend fun getSimilarVacanciesByParams(params: List<String>): Outcome<List<VacancyShortSimilar>>

    suspend fun getSimilarityParams(id: String): Outcome<SimilarityParams>
}