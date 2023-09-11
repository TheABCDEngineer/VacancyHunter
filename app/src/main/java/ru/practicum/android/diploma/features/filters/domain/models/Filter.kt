package ru.practicum.android.diploma.features.filters.domain.models

data class Filter(
    var industry: Industry?,
    var country: Country?,
    val region: Region?,
    var salary: String?,
    var doNotShowWithoutSalary: Boolean
)
