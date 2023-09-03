package ru.practicum.android.diploma.features.filters.domain

import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.root.domain.model.Outcome

interface FiltersInteractor {
    suspend fun getIndustries(): Outcome<List<Industry>>
}