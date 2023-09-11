package ru.practicum.android.diploma.root.data

import ru.practicum.android.diploma.features.filters.data.models.FiltersMapper
import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.root.data.network.NetworkSearch
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.domain.model.Outcome
import ru.practicum.android.diploma.root.domain.repository.FilterRepository

class FilterImplSharedPreference(
    private val filtersMapper: FiltersMapper,
    private val networkClient: NetworkSearch
) : FilterRepository {
    override suspend fun getIndustries(): Outcome<List<Industry>> {
        val response = networkClient.getIndustries()
        return when (response.resultCode) {
            NetworkResultCode.SUCCESS -> {
                if (response.data != null) {
                    val industries = filtersMapper.getIndustriesFromSubindustries(response.data!!)
                    Outcome.Success(data = industries)
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
}