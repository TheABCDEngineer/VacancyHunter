package ru.practicum.android.diploma.features.search.domain.model

import ru.practicum.android.diploma.root.presentation.model.VacancyScreenModel

data class ResponseModel(
    val resultVacancyList: ArrayList<VacancyScreenModel>,
    val foundCount: Int,
    val pagesCount: Int,
    val page: Int
)