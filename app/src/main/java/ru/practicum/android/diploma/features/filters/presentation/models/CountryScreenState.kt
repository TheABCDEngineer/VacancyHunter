package ru.practicum.android.diploma.features.filters.presentation.models

import ru.practicum.android.diploma.features.filters.domain.models.Area

sealed class CountryScreenState {
    object Loading : CountryScreenState()

    object Error : CountryScreenState()

    data class Content(
        val countries: List<Area>,
    ) : CountryScreenState()
}