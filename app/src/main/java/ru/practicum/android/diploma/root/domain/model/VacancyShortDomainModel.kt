package ru.practicum.android.diploma.root.domain.model

import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary


data class VacancyShortDomainModel(
    val vacancyId: String,
    val vacancyName: String,
    val salary: Salary?,
    val logoUrl: String,
    val employerName: String,
    val employerArea: String
)