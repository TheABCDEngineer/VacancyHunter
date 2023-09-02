package ru.practicum.android.diploma.features.similarvacancies.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.features.similarvacancies.domain.models.SimilarityParams
import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.root.data.Outcome
import ru.practicum.android.diploma.root.domain.VacancyRepository

class SimilarVacanciesInteractorImpl(
    private val vacancyRepository: VacancyRepository
) : SimilarVacanciesInteractor {
    override suspend fun getSimilarVacancies(vacancyId: String): Flow<Outcome<List<VacancyShortSimilar>>> = flow {

        val searchParams = mutableListOf<String>(vacancyId)

        val similarityParams = getSimilarityParams(id = vacancyId)

        similarityParams.data?.let {
            // pass desirable similarity parameters from parameters model
            searchParams.addAll(similarityParams.data.profRoles)
        }

        val outcome = vacancyRepository.getSimilarVacanciesByParams(searchParams.toList())
        emit(outcome)
    }

    private suspend fun getSimilarityParams(id: String): Outcome<SimilarityParams> {
        return vacancyRepository.getSimilarityParams(id)
    }

}