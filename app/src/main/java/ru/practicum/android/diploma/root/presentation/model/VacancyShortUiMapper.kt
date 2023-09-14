package ru.practicum.android.diploma.root.presentation.model

import android.content.Context
import ru.practicum.android.diploma.root.domain.model.VacancyShortDomainModel
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary
import ru.practicum.android.diploma.root.presentation.ui.SalaryFormat

class VacancyShortUiMapper(private val context: Context) : (VacancyShortDomainModel) -> VacancyShortUiModel {

    override fun invoke(domainModel: VacancyShortDomainModel): VacancyShortUiModel {
        return VacancyShortUiModel(
            vacancyId = domainModel.vacancyId,
            cardTitle = formatTitle(domainModel.vacancyName, domainModel.employerArea),
            employerName = domainModel.employerName,
            salary = formatSalary(domainModel.salary),
            logoUrl = domainModel.logoUrl
        )
    }

    private fun formatTitle(title: String, area: String): String {
        val list = listOf(title, area).filter { it.isNotEmpty() }
        return list.joinToString(", ")
    }

    private fun formatSalary(salaryObj: Salary?): String {
        return SalaryFormat.formatSalaryString(salaryObj, context)
    }

}
