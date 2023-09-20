package ru.practicum.android.diploma.features.search.presentation.screenState

enum class SearchingCleanerState(
    val isCleaner: Boolean,
    val isPlaceholder: Boolean
) {
    ACTIVE(
        isCleaner = true,
        isPlaceholder = false
    ),

    INACTIVE(
        isCleaner = false,
        isPlaceholder = true
    )
}