package ru.practicum.android.diploma.features.similarvacancies.data.models

import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary

class SimilarVacanciesMapper : (SimilarVacanciesResponse) -> List<VacancyShortSimilar> {

    override fun invoke(dto: SimilarVacanciesResponse): List<VacancyShortSimilar> {
        return dto.similarVacanciesList.map { vacancy ->
            VacancyShortSimilar(
                vacancyId = vacancy.vacancyId,
                vacancyName = vacancy.vacancyName ?: "",
                salary = getSalary(vacancy.salary),
                logoUrl = vacancy.employer?.logoUrls?.logoUrl90 ?: "",
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