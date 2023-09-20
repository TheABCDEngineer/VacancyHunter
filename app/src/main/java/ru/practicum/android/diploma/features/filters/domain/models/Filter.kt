package ru.practicum.android.diploma.features.filters.domain.models

data class Filter(
    val industry: Industry?,
    val country: Area?,
    val region: Area?,
    val salary: String?,
    val doNotShowWithoutSalary: Boolean
)
