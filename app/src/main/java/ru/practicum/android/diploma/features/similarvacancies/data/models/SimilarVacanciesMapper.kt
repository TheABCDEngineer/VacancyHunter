package ru.practicum.android.diploma.features.similarvacancies.data.models

import ru.practicum.android.diploma.root.domain.model.VacancyShortDomainModel
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary
import ru.practicum.android.diploma.root.data.network.models.Response

class SimilarVacanciesMapper : (Response) -> List<VacancyShortDomainModel> {

    override fun invoke(dto: Response): List<VacancyShortDomainModel> {
        dto as SimilarVacanciesResponse
        return dto.similarVacanciesList.map { vacancy ->
            VacancyShortDomainModel(
                vacancyId = vacancy.vacancyId,
                vacancyName = vacancy.vacancyName ?: "",
                salary = getSalary(vacancy.salary),
                logoUrl = vacancy.employer?.logoUrls?.logoOriginal ?: "",
                employerName = vacancy.employer?.employerName ?: "",
                employerArea = vacancy.vacancyArea?.area ?: ""
            )
        }
    }

    private fun getSalary(salaryDto: VacancyDetailsDto.SalaryDto?): Salary? {
        return when (salaryDto) {
            null -> null
            else -> Salary(
                salaryCurrency = salaryDto.currency,
                salaryLowerBoundary = salaryDto.from,
                salaryUpperBoundary = salaryDto.to
            )
        }
    }
}