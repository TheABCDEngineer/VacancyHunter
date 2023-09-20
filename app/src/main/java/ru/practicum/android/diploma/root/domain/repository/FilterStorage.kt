package ru.practicum.android.diploma.root.domain.repository

import ru.practicum.android.diploma.features.filters.domain.models.Filter

interface FilterStorage {
   fun saveFilter(model: Filter?)
    fun getFilter(): Filter?
}