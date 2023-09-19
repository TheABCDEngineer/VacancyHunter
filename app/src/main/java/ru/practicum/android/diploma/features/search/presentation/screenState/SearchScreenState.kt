package ru.practicum.android.diploma.features.search.presentation.screenState

enum class SearchScreenState (
    val isChip: Boolean,
    val isProgressBar: Boolean = false,
    val isPlaceholder: Boolean = false
) {
    NEWBORN(
        isChip = false,
        isPlaceholder = true
    ),

    SEARCHING(
        isChip = false,
        isProgressBar = true
    ),

    EMPTY_RESULT(
        isChip = true
    ),

    SOMETHING_WENT_WRONG(
        isChip = true
    ),

    RESPONSE_RESULTS(
        isChip = true
    ),

    NO_INTERNET_CONNECTION(
        isChip = true
    )
}