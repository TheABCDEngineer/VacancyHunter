package ru.practicum.android.diploma.features.search.presentation.ui.model

import ru.practicum.android.diploma.root.presentation.model.VacancyScreenModel

data class VacancyFactoryModel(
    val isNewSearching: Boolean,
    val items: ArrayList<VacancyScreenModel>,
    val isContinueLoading: Boolean
)
