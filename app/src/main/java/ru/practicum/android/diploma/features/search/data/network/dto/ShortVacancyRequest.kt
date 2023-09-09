package ru.practicum.android.diploma.features.search.data.network.dto

data class ShortVacancyRequest(
    val requestJob: String,
    val countryId: String?,
    val regionId: String?,
    val industryId: String?,
    val salary: String?,
    val isSalary: Boolean
)