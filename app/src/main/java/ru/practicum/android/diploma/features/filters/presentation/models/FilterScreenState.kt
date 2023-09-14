package ru.practicum.android.diploma.features.filters.presentation.models

import ru.practicum.android.diploma.features.filters.domain.models.Area
import ru.practicum.android.diploma.features.filters.domain.models.Industry

sealed interface FilterScreenState {
    object MainScreen : FilterScreenState
    data class IndustryScreen(val industry:Industry?) : FilterScreenState
    data class WorkPlaceScreen(val country: Area?, val region: Area?) : FilterScreenState
    data class CountryScreen(val country: Area?): FilterScreenState
}
