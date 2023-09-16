package ru.practicum.android.diploma.features.filters.presentation.models

import ru.practicum.android.diploma.features.filters.domain.models.Area
import ru.practicum.android.diploma.features.filters.domain.models.Industry

sealed interface FilterScreenState {
    object MainScreen: FilterScreenState
    object IndustryScreen: FilterScreenState
    object WorkPlaceScreen: FilterScreenState
    object CountryScreen: FilterScreenState
    object RegionScreen: FilterScreenState
}
