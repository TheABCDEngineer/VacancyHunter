package ru.practicum.android.diploma.features.similarvacancies.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.root.domain.model.Outcome

interface SimilarVacanciesInteractor {

    suspend fun getSimilarVacancies(vacancyId: String): Flow<Outcome<List<VacancyShortSimilar>>>

}