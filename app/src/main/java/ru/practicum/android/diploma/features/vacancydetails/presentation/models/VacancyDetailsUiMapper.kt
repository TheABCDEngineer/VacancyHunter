package ru.practicum.android.diploma.features.vacancydetails.presentation.models

import android.content.Context
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.presentation.ui.SalaryFormat

class VacancyDetailsUiMapper(private val context: Context) :
        (VacancyDetails) -> VacancyDetailsUiModel {

    override fun invoke(model: VacancyDetails): VacancyDetailsUiModel {
        return VacancyDetailsUiModel(
            vacancyId = model.vacancyId,
            vacancyName = model.vacancyName,
            salary = formatSalaryString(model.salary),
            logoUrl = model.logoUrl,
            employerName = model.employerName,
            employerArea = model.employerArea,
            experience = model.experienceReq,
            employmentTypes = formatEmploymentTypes(model.employmentType, model.scheduleType),
            vacancyDescription = chooseDescription(
                model.vacancyDescription,
                model.vacancyBrandedDesc
            ),
            keySkills = formatKeySkills(model.keySkills),
            contactsName = model.contactsName,
            contactsEmail = model.contactsEmail,
            contactsPhones = model.contactsPhones,
            shareVacancyUrl = model.shareVacancyUrl
        )
    }

    private fun formatKeySkills(keySkills: List<String>): String {
        return keySkills
            .filter { it.isNotEmpty() }
            .joinToString("\n")
    }

    private fun chooseDescription(vacancyDescription: String, vacancyBrandedDesc: String): String {
        return if (vacancyBrandedDesc.isNotEmpty()) vacancyBrandedDesc else vacancyDescription
    }

    private fun formatEmploymentTypes(employmentType: String, scheduleType: String): String {
        return listOf(employmentType, scheduleType)
            .filter { it.isNotEmpty() }
            .joinToString(", ")
    }

    private fun formatSalaryString(salaryObj: Salary?): String {
        if (salaryObj == null) return ""
        return SalaryFormat.formatSalaryString(salaryObj, context)
    }

}