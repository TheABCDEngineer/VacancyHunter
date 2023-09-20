package ru.practicum.android.diploma.features.similarvacancies.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.features.similarvacancies.domain.models.SimilarityParams
import ru.practicum.android.diploma.root.domain.model.VacancyShortDomainModel
import ru.practicum.android.diploma.root.domain.model.Outcome
import ru.practicum.android.diploma.root.domain.VacancyRepository

class SimilarVacanciesInteractorImpl(
    private val vacancyRepository: VacancyRepository
) : SimilarVacanciesInteractor {
    override suspend fun getSimilarVacancies(vacancyId: String): Flow<Outcome<List<VacancyShortDomainModel>>> = flow {

        val similarityParams = getSimilarityParams(id = vacancyId)

        val searchParams = similarityParams.data ?: SimilarityParams(vacancyId = vacancyId, null)

        if (searchParams.profRoles == null) {
            vacancyRepository.getSimilarVacanciesById(searchParams)
        } else {
            vacancyRepository.getSimilarVacanciesByProfRoles(searchParams)
        }

        val outcome = vacancyRepository.getSimilarVacanciesByProfRoles(searchParams)
        emit(outcome)
    }

    private suspend fun getSimilarityParams(id: String): Outcome<SimilarityParams> {
        return vacancyRepository.getSimilarityParams(id)
    }

}