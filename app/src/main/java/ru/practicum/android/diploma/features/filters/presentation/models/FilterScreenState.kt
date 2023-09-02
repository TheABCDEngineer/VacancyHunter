package ru.practicum.android.diploma.features.filters.presentation.models

import ru.practicum.android.diploma.features.filters.domain.models.Country
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.features.filters.domain.models.Region

sealed interface FilterScreenState {
    object MainScreen : FilterScreenState
    data class IndustryScreen(val industry: Industry?) : FilterScreenState
    data class WorkPlaceScreen(val country: Country?, val region: Region?) : FilterScreenState
}
