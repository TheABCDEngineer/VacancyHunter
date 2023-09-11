package ru.practicum.android.diploma.features.search.presentation.screenState

sealed class FilterState(val filterRequirementsCount: Int = 0) {
    class Inactive(): FilterState()
    class Active(filterRequirementsCount: Int): FilterState(filterRequirementsCount)
}
