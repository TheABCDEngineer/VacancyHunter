package ru.practicum.android.diploma.root.domain.repository

import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.root.domain.model.Outcome

interface FilterRepository {
    fun saveFilter(model: Filter)
    fun getFilter(): Filter?
    suspend fun getIndustries(): Outcome<List<Industry>>
}