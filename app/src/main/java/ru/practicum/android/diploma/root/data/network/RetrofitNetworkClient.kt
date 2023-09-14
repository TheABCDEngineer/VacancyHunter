package ru.practicum.android.diploma.root.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesRequest
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesResponse
import ru.practicum.android.diploma.features.filters.data.dto.IndustryDto
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsRequest
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.data.network.models.Response

class RetrofitNetworkClient(
    private val api: HeadHunterApi,
    private val context: Context
) : NetworkSearch {

    override suspend fun getVacancyById(dto: VacancyDetailsRequest): Response<VacancyDetailsDto> {

        if (!isConnected()) {
            return Response(resultCode = NetworkResultCode.CONNECTION_ERROR, data = null)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = api.getVacancyById(vacancyId = dto.id)
                Response(resultCode = NetworkResultCode.SUCCESS, response )
            } catch (e: Throwable) {
                Response(resultCode = NetworkResultCode.SERVER_ERROR, data = null)
            }
        }
    }

    override suspend fun getSimilarVacanciesById(dto: SimilarVacanciesRequest): Response<SimilarVacanciesResponse> {
        if (!isConnected()) {
            return Response(resultCode = NetworkResultCode.CONNECTION_ERROR, data = null)
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getSimilarVacanciesById(
                    dto.params.vacancyId
                )
                Response(resultCode = NetworkResultCode.SUCCESS, response )
            } catch (e: Throwable) {
                Response(resultCode = NetworkResultCode.SERVER_ERROR, data = null)
            }
        }
    }

    override suspend fun getSimilarVacanciesByProfRoles(dto: SimilarVacanciesRequest): Response<SimilarVacanciesResponse> {
        if (!isConnected()) {
            return Response(resultCode = NetworkResultCode.CONNECTION_ERROR, data = null)
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getSimilarVacanciesByProfRoles(
                    dto.params.vacancyId,
                    dto.params.profRoles!![0]
                )
                Response(resultCode = NetworkResultCode.SUCCESS, response)

            } catch (e: Throwable) {
                Response(resultCode = NetworkResultCode.SERVER_ERROR, data = null)
            }
        }
    }
    
    override suspend fun getIndustries(): Response<List<IndustryDto>> {
        if (!isConnected()) {
            return Response(resultCode = NetworkResultCode.CONNECTION_ERROR, data = null)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = api.getIndustries()
                Response(resultCode = NetworkResultCode.SUCCESS, response)
            } catch (e: Throwable) {
                Response(resultCode = NetworkResultCode.SERVER_ERROR, data = null)
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}