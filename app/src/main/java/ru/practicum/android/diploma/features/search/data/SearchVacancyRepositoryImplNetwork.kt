package ru.practicum.android.diploma.features.search.data

import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.root.data.network.NetworkClient
import ru.practicum.android.diploma.features.search.data.network.dto.ShortVacancyRequest
import ru.practicum.android.diploma.features.search.data.network.dto.VacancyResponse
import ru.practicum.android.diploma.features.search.domain.model.ResponseModel
import ru.practicum.android.diploma.features.search.domain.repository.SearchVacancyRepository
import ru.practicum.android.diploma.root.data.DataConverter
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.domain.model.Outcome

class SearchVacancyRepositoryImplNetwork(
    private val networkClient: NetworkClient,
    private val converter: DataConverter
) : SearchVacancyRepository {

    override suspend fun loadVacancies(
        value: String,
        filterParams: Filter,
        perPage: Int,
        page: Int
    ): Outcome<ResponseModel> {
        val response = networkClient.executeRequest(
            fetchRequest(value, filterParams, perPage, page)
        )

        return when (response.resultCode) {
            NetworkResultCode.SUCCESS -> {
                if (response.data != null) {
                    val data = response.data as VacancyResponse
                    val vacancyResponse = ResponseModel(
                        resultVacancyList = converter.map(listDto = data.items),
                        foundCount = data.found,
                        pagesCount = data.pages,
                        page = data.page
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

    private fun fetchRequest(
        requestJob: String,
        filter: Filter,
        perPage: Int,
        page: Int
    ): ShortVacancyRequest {
        return ShortVacancyRequest(
           requestJob = requestJob,
           countryId = filter.country?.id.toString(),
           regionId = if (filter.region != null) filter.region.id.toString() else null,
           industryId = filter.industry?.id,
           salary = filter.salary,
           isSalary = filter.doNotShowWithoutSalary,
           perPage = perPage,
           page = page
       )
    }
}