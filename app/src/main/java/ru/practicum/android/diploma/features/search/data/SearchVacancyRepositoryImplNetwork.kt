package ru.practicum.android.diploma.features.search.data

import android.util.Log
import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.features.search.data.network.dto.ShortVacancyRequest
import ru.practicum.android.diploma.features.search.data.network.dto.VacancyResponse
import ru.practicum.android.diploma.features.search.domain.model.ResponseModel
import ru.practicum.android.diploma.features.search.domain.repository.SearchVacancyRepository
import ru.practicum.android.diploma.root.data.DataConverter
import ru.practicum.android.diploma.root.data.Outcome
import ru.practicum.android.diploma.root.data.network.NetworkSearch
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode

class SearchVacancyRepositoryImplNetwork(
    private val networkClient: NetworkSearch,
    private val converter: DataConverter
) : SearchVacancyRepository {

    override suspend fun loadVacancies(
        value: String,
        filterParams: Filter
    ): Outcome<ResponseModel> {
        val response = networkClient.executeRequest(
            ShortVacancyRequest(
                fetchRequestPath(value, filterParams)
            )
        )

        return when (response.resultCode) {
            NetworkResultCode.SUCCESS -> {
                if (response.data != null) {
                    val vacancyResponse = ResponseModel(
                        converter.map(listDto = (response.data!! as VacancyResponse).items)
                    )
                    Outcome.Success(data = vacancyResponse)
                } else {
                    Outcome.Error(status = NetworkResultCode.SERVER_ERROR, data = null)
                }
            }

            NetworkResultCode.CONNECTION_ERROR -> {
                Outcome.Error(status = NetworkResultCode.CONNECTION_ERROR, data = null)
            }

            else -> {
                Outcome.Error(status = NetworkResultCode.UNKNOWN_ERROR, data = null)
            }
        }
    }

    private fun fetchRequestPath(value: String, filter: Filter): String {
        var path = "text=$value"
        if (filter.country != null) path += "&country=${filter.country.id}"
        if (filter.region != null) path += "&area=${filter.region.id}"
        if (filter.industry != null) path += "&industry=${filter.industry.id}"
        if (filter.salary != null) path += "&salary=${filter.salary}"
        if (filter.doNotShowWithoutSalary != null) path += "&only_with_salary=${filter.doNotShowWithoutSalary}"
        return path
    }
}