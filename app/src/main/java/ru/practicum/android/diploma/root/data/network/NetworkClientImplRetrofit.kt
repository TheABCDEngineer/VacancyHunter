package ru.practicum.android.diploma.root.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.features.filters.data.dto.network.AreasRequest
import ru.practicum.android.diploma.features.filters.data.dto.network.AreasResponse
import ru.practicum.android.diploma.features.filters.data.dto.network.IndustriesRequest
import ru.practicum.android.diploma.features.filters.data.dto.network.IndustriesResponse
import ru.practicum.android.diploma.features.search.data.network.dto.ShortVacancyRequest
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesRequest
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsRequest
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.data.network.models.Request
import ru.practicum.android.diploma.root.data.network.models.RequestArg
import ru.practicum.android.diploma.root.data.network.models.Response
import ru.practicum.android.diploma.root.data.network.models.ResponseData
import ru.practicum.android.diploma.util.isInternetConnected

class NetworkClientImplRetrofit(
    private val api: HeadHunterApi,
    private val context: Context
) : NetworkClient {
    override suspend fun executeRequest(request: Request): ResponseData<Response> {
        if (!isInternetConnected(context)) {
            return ResponseData(resultCode = NetworkResultCode.CONNECTION_ERROR, data = null)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = provideRequest(request)
                ResponseData(resultCode = NetworkResultCode.SUCCESS, response )
            } catch (e: Throwable) {
                ResponseData(resultCode = NetworkResultCode.SERVER_ERROR, data = null)
            }
        }
    }

    private suspend fun provideRequest(request: Request): Response? {
        return when (request) {
            is ShortVacancyRequest -> {
                api.getVacancyListByParameters(
                    requestJob = request.requestJob,
                    countryId = request.countryId,
                    regionId = request.regionId,
                    industryId = request.industryId,
                    salary = request.salary,
                    isSalary = request.isSalary.toString(),
                    perPage = request.perPage,
                    page = request.page
                )
            }
            is VacancyDetailsRequest -> api.getVacancyById(vacancyId = request.id)

            is SimilarVacanciesRequest -> {
                when(request.requestArg) {
                    RequestArg.GET_BY_ID -> api.getSimilarVacanciesById(request.params.vacancyId)
                    RequestArg.GET_BY_PROF_ROLES -> api.getSimilarVacanciesByProfRoles(
                        vacancyId = request.params.vacancyId,
                        role = request.params.profRoles?.firstOrNull() ?: ""
                    )
                    else -> null
                }
            }

            is IndustriesRequest -> IndustriesResponse(api.getIndustries())
            is AreasRequest -> AreasResponse(api.getAreas())

            else -> null
        }
    }
}