package ru.practicum.android.diploma.features.filters.domain

import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.root.domain.model.Outcome
import ru.practicum.android.diploma.root.domain.repository.FilterRepository

class FiltersInteractorImpl(private val filtersRepository: FilterRepository): FiltersInteractor {
    override suspend fun getIndustries(): Outcome<List<Industry>> {
        return filtersRepository.getIndustries()
    }

}