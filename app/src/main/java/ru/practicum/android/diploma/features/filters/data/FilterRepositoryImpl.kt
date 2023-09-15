package ru.practicum.android.diploma.features.filters.data

import ru.practicum.android.diploma.features.filters.data.models.FiltersMapper
import ru.practicum.android.diploma.features.filters.domain.models.Area
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.root.data.network.NetworkSearch
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.domain.model.Outcome
import ru.practicum.android.diploma.features.filters.domain.FilterRepository

class FilterRepositoryImpl(
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

    override suspend fun getRegions(parentId: Int?): Outcome<List<Area>> {
        val response = networkClient.getAreas()
        return when (response.resultCode) {
            NetworkResultCode.SUCCESS -> {
                if (response.data != null) {
                    val areas = if (parentId == null)
                        filtersMapper.convertAreaTreeDto(response.data!!)
                    else
                        filtersMapper.convertAreaTreeByParentIdDto(response.data!!, parentId)
                    Outcome.Success(data = areas.sortedBy { it.name })
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

    override suspend fun getCountries(): Outcome<List<Area>> {
        val response = networkClient.getAreas()
        return when (response.resultCode) {
            NetworkResultCode.SUCCESS -> {
                if (response.data != null) {
                    val areas = filtersMapper.convertAreaTreeFirstLevelDto(response.data!!)
                    Outcome.Success(data = areas)
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