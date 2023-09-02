package ru.practicum.android.diploma.root.domain

import ru.practicum.android.diploma.features.similarvacancies.domain.models.SimilarityParams
import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.domain.model.Outcome

interface VacancyRepository {
    suspend fun getVacancyById(id: String): Outcome<VacancyDetails>

    suspend fun getSimilarVacanciesById(searchParams: SimilarityParams): Outcome<List<VacancyShortSimilar>>

    suspend fun getSimilarVacanciesByProfRoles(params: SimilarityParams): Outcome<List<VacancyShortSimilar>>

    suspend fun getSimilarityParams(id: String): Outcome<SimilarityParams>


}