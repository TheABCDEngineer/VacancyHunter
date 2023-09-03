package ru.practicum.android.diploma.features.similarvacancies.domain.models

import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary


data class VacancyShortSimilar(
    val vacancyId: String,
    val vacancyName: String,
    val salary: Salary?,
    val logoUrl: String,
    val employerName: String,
    val employerArea: String
)