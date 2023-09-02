package ru.practicum.android.diploma.root.data

import com.google.gson.Gson
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesMapper
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesRequest
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarityParamsMapper
import ru.practicum.android.diploma.features.similarvacancies.domain.models.SimilarityParams
import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsMapper
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsRequest
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.data.network.NetworkSearch
import ru.practicum.android.diploma.root.data.network.processResponse
import ru.practicum.android.diploma.root.domain.VacancyRepository
import ru.practicum.android.diploma.root.domain.model.Outcome

class VacancyRepositoryImpl(
    private val detailsMapper: VacancyDetailsMapper,
    private val simParamsMapper: SimilarityParamsMapper,
    private val similarVacanciesMapper: SimilarVacanciesMapper,
    private val networkClient: NetworkSearch,
    val gson: Gson
) : VacancyRepository {
    override suspend fun getVacancyById(id: String): Outcome<VacancyDetails> {
        val response = networkClient.getVacancyById(dto = VacancyDetailsRequest(id))
        return processResponse(response, detailsMapper)
    }

    override suspend fun getSimilarVacanciesById(
        searchParams: SimilarityParams
    ): Outcome<List<VacancyShortSimilar>> {
        val response =
            networkClient.getSimilarVacanciesById(dto = SimilarVacanciesRequest(searchParams))
        return processResponse(response, similarVacanciesMapper)
    }

    override suspend fun getSimilarVacanciesByProfRoles(
        params: SimilarityParams
    ): Outcome<List<VacancyShortSimilar>> {
        val response =
            networkClient.getSimilarVacanciesByProfRoles(dto = SimilarVacanciesRequest(params))
        return processResponse(response, similarVacanciesMapper)
    }

    override suspend fun getSimilarityParams(id: String): Outcome<SimilarityParams> {
        val response = networkClient.getVacancyById(dto = VacancyDetailsRequest(id))
        return processResponse(response, simParamsMapper)
    }
}