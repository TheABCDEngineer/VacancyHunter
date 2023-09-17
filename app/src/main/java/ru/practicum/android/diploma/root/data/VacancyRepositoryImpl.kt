package ru.practicum.android.diploma.root.data

import ru.practicum.android.diploma.root.data.network.NetworkClient
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesMapper
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesRequest
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarityParamsMapper
import ru.practicum.android.diploma.features.similarvacancies.domain.models.SimilarityParams
import ru.practicum.android.diploma.root.domain.model.VacancyShortDomainModel
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsMapper
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsRequest
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.data.network.ResponseProcessor
import ru.practicum.android.diploma.root.data.network.models.RequestArg
import ru.practicum.android.diploma.root.domain.VacancyRepository
import ru.practicum.android.diploma.root.domain.model.Outcome

class VacancyRepositoryImpl(
    private val detailsMapper: VacancyDetailsMapper,
    private val simParamsMapper: SimilarityParamsMapper,
    private val similarVacanciesMapper: SimilarVacanciesMapper,
    private val responseProcessor: ResponseProcessor,
    private val networkClient: NetworkClient
) : VacancyRepository {
    override suspend fun getVacancyById(id: String): Outcome<VacancyDetails> {
        val response = networkClient.executeRequest(VacancyDetailsRequest(id))
        return responseProcessor.processResponse(response, detailsMapper)
    }

    override suspend fun getSimilarVacanciesById(
        searchParams: SimilarityParams
    ): Outcome<List<VacancyShortDomainModel>> {
        val response = networkClient.executeRequest(
            SimilarVacanciesRequest(searchParams).apply { requestArg = RequestArg.GET_BY_ID }
        )
        return responseProcessor.processResponse(response, similarVacanciesMapper)
    }

    override suspend fun getSimilarVacanciesByProfRoles(
        params: SimilarityParams
    ): Outcome<List<VacancyShortDomainModel>> {
        val response =
            networkClient.executeRequest(
                SimilarVacanciesRequest(params).apply { requestArg = RequestArg.GET_BY_PROF_ROLES }
            )
        return responseProcessor.processResponse(response, similarVacanciesMapper)
    }

    override suspend fun getSimilarityParams(id: String): Outcome<SimilarityParams> {
        val response =
            networkClient.executeRequest(VacancyDetailsRequest(id))
        return responseProcessor.processResponse(response, simParamsMapper)
    }
}