package ru.practicum.android.diploma.root.presentation.ui.adapters

import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiModel

interface VacancyClickListener {
    fun onListItemClick(vacancy: VacancyShortUiModel)
}