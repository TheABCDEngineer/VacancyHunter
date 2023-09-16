package ru.practicum.android.diploma.root.data

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.search.data.network.dto.VacancyShortDto
import ru.practicum.android.diploma.root.domain.model.VacancyShortModel
import ru.practicum.android.diploma.root.presentation.model.VacancyScreenModel
import ru.practicum.android.diploma.root.presentation.ui.CurrencySymbol
import ru.practicum.android.diploma.root.presentation.ui.SalaryFormat
import ru.practicum.android.diploma.util.modifyNumberWithDigits

class DataConverter(
    private val context: Context
) {
    fun map(dto: VacancyShortDto): VacancyShortModel {
        return VacancyShortModel(
            id = dto.id,
            employer = dto.employer.employerName,
            artworkUrl = dto.employer.logoUrls?.logoUrl90,
            jobTitle = dto.jobTitle,
            region = dto.region.area,
            salaryFrom = dto.salary?.from,
            salaryTo = dto.salary?.to,
            salaryCurrency = dto.salary?.currency,
        )
    }

    fun map(model: VacancyShortModel): VacancyScreenModel {
        val salaryFrom = model.salaryFrom?.let {
            context.getString(R.string.from) + " ${modifyNumberWithDigits(it)} "} ?: ""

        val salaryTo = model.salaryTo?.let {
            context.getString(R.string.to) + " ${modifyNumberWithDigits(it)} " } ?: ""

        val currency = model.salaryCurrency?.let { CurrencySymbol.getCurrencySymbol(it) } ?: ""

        var salary = salaryFrom + salaryTo + currency
        if (salaryFrom.isEmpty() && salaryTo.isEmpty()) salary =
            context.getString(R.string.no_salary)

        val jobTitle = "${model.jobTitle}, ${model.region}"
        val details = "${model.employer}\n" + salary

        return VacancyScreenModel(
            id = model.id,
            artworkUrl = model.artworkUrl ?: "",
            job = jobTitle,
            details = details
        )
    }

    fun map(listDto: ArrayList<VacancyShortDto>): ArrayList<VacancyScreenModel> {
        val resultList = ArrayList<VacancyScreenModel>()
        for (model in listDto) {
            val vacancyShortModel = map(model)
            resultList.add(
                map(vacancyShortModel)
            )
        }
        return resultList
    }
}