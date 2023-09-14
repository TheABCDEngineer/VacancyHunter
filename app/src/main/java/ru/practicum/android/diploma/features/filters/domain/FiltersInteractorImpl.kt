package ru.practicum.android.diploma.features.filters.domain

import ru.practicum.android.diploma.features.filters.domain.models.Area
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.root.domain.model.Outcome

class FiltersInteractorImpl(private val filtersRepository: FilterRepository): FiltersInteractor {
    override suspend fun getIndustries(): Outcome<List<Industry>> {
        return filtersRepository.getIndustries()
    }

    override suspend fun getRegions(parentId: Int?): Outcome<List<Area>> {
        return filtersRepository.getRegions(parentId)
    }

    override suspend fun getCountries(): Outcome<List<Area>> {
        return filtersRepository.getCountries()
    }

}