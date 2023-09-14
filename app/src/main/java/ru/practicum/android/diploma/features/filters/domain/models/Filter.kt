package ru.practicum.android.diploma.features.filters.domain.models

data class Filter(
    var industry: Industry?,
    val country: Area?,
    val region: Area?,
    var salary: String?,
    var doNotShowWithoutSalary: Boolean
)
