package ru.practicum.android.diploma.root.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.features.search.data.network.dto.ShortVacancyRequest
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsRequest
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.data.network.models.Response
import ru.practicum.android.diploma.util.isInternetConnected

class RetrofitNetworkClient(
    private val api: HeadHunterApi,
    private val context: Context
) : NetworkSearch {
    override suspend fun executeRequest(request: Any): Response<Any> {
        if (!isInternetConnected(context)) {
            return Response(resultCode = NetworkResultCode.CONNECTION_ERROR, data = null)
        }

        request as ShortVacancyRequest

        return withContext(Dispatchers.IO) {
            try {
                val response = provideRequest(request)
                Response(resultCode = NetworkResultCode.SUCCESS, response )
            } catch (e: Throwable) {
                Response(resultCode = NetworkResultCode.SERVER_ERROR, data = null)
            }
        }
    }

    private suspend fun provideRequest(request: Any): Any {
        return when (request) {
            is VacancyDetailsRequest -> api.getVacancyById(vacancyId = request.id)
            is ShortVacancyRequest -> api.getVacancyListByParameters(request.path)
            else -> {}
        }
    }
}