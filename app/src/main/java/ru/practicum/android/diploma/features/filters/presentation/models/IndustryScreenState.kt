package ru.practicum.android.diploma.features.filters.presentation.models

import ru.practicum.android.diploma.features.filters.domain.models.Industry


sealed class IndustryScreenState {
    object Loading : IndustryScreenState()

    object Error : IndustryScreenState()

    data class Content(
        val industries: List<Industry>,
    ) : IndustryScreenState()
}
