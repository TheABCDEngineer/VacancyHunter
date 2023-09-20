package ru.practicum.android.diploma.features.vacancydetails.presentation.models

import ru.practicum.android.diploma.features.vacancydetails.domain.models.ContactPhone

data class VacancyDetailsUiModel(
    val vacancyId: String,
    val vacancyName: String,
    val salary: String,
    val logoUrl: String,
    val employerName: String,
    val employerAddress: String,
    val experience: String,
    val employmentTypes: String,
    val vacancyDescription: String,
    val keySkills: String,
    val contactsName: String,
    val contactsEmail: String,
    val contactsPhones: List<ContactPhone>,
    val shareVacancyUrl: String,
    val isFavorite: Boolean
)