package ru.practicum.android.diploma.features.search.domain.repository

import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.features.search.domain.model.ResponseModel
import ru.practicum.android.diploma.root.domain.model.Outcome

interface SearchVacancyRepository {
    suspend fun loadVacancies(value: String, filterParams: Filter, perPage: Int, page: Int): Outcome<ResponseModel>
}