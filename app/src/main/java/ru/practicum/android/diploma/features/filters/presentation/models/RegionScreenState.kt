package ru.practicum.android.diploma.features.filters.presentation.models

import ru.practicum.android.diploma.features.filters.domain.models.Area

sealed class RegionScreenState {
    object Loading : RegionScreenState()

    object Error : RegionScreenState()

    data class Content(
        val regions: List<Area>,
    ) : RegionScreenState()
}
