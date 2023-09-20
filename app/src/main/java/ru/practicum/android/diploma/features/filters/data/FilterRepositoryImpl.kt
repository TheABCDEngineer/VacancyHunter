package ru.practicum.android.diploma.features.filters.data

import ru.practicum.android.diploma.features.filters.data.dto.network.AreasRequest
import ru.practicum.android.diploma.features.filters.data.dto.network.AreasResponse
import ru.practicum.android.diploma.features.filters.data.dto.network.IndustriesRequest
import ru.practicum.android.diploma.features.filters.data.dto.network.IndustriesResponse
import ru.practicum.android.diploma.features.filters.data.models.FiltersMapper
import ru.practicum.android.diploma.features.filters.domain.models.Area
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.domain.model.Outcome
import ru.practicum.android.diploma.features.filters.domain.FilterRepository
import ru.practicum.android.diploma.root.data.network.NetworkClient
import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.root.domain.repository.FilterStorage

class FilterRepositoryImpl(
    private val filtersMapper: FiltersMapper,
    private val networkClient: NetworkClient,
    private val filterStorage: FilterStorage
) : FilterRepository {
    override suspend fun getIndustries(): Outcome<List<Industry>> {
        val response =
            networkClient.executeRequest(IndustriesRequest())
        return when (response.resultCode) {
            NetworkResultCode.SUCCESS -> {
                if (response.data != null) {
                    val data = response.data as IndustriesResponse
                    val industries = filtersMapper.getIndustriesFromSubindustries(data.industries)
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
        val response =
            networkClient.executeRequest(AreasRequest())
        return when (response.resultCode) {
            NetworkResultCode.SUCCESS -> {
                if (response.data != null) {
                    val data = response.data as AreasResponse
                    val areas = if (parentId == null)
                        filtersMapper.convertAreaTreeDto(data.areas)
                    else
                        filtersMapper.convertAreaTreeByParentIdDto(data.areas, parentId)
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
        val response =
            networkClient.executeRequest(AreasRequest())
        return when (response.resultCode) {
            NetworkResultCode.SUCCESS -> {
                if (response.data != null) {
                    val data = response.data as AreasResponse
                    val areas = filtersMapper.convertAreaTreeFirstLevelDto(data.areas)
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

    override fun saveFilters(filter: Filter?) {
        filterStorage.saveFilter(filter)
    }

    override fun getSavedFilters(): Outcome<Filter> {
        val savedFilters = filterStorage.getFilter()
        return when(savedFilters) {
            null -> Outcome.Error(status = NetworkResultCode.UNKNOWN_ERROR, data = null)
            else -> Outcome.Success(data = savedFilters)
        }
    }
}