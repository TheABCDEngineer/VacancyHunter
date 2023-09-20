package ru.practicum.android.diploma.features.search.data.network.dto

import ru.practicum.android.diploma.root.data.network.models.Request

data class ShortVacancyRequest(
    val requestJob: String,
    val countryId: String?,
    val regionId: String?,
    val industryId: String?,
    val salary: String?,
    val isSalary: Boolean,
    val perPage: Int,
    val page: Int
): Request()