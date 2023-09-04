package ru.practicum.android.diploma.features.similarvacancies.presentation.models

import android.content.Context
import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary
import ru.practicum.android.diploma.root.presentation.ui.SalaryFormat

class VacancySimilarShortUiMapper(private val context: Context) : (VacancyShortSimilar) -> VacancySimilarShortUiModel {

    override fun invoke(domainModel: VacancyShortSimilar): VacancySimilarShortUiModel {
        return VacancySimilarShortUiModel(
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
        return salaryObj?.let {
            SalaryFormat.formatSalaryString(salaryObj, context)
        } ?: ""
    }

}
