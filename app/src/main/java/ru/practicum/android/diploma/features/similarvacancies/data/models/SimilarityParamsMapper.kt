package ru.practicum.android.diploma.features.similarvacancies.data.models

import ru.practicum.android.diploma.features.similarvacancies.domain.models.SimilarityParams
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto

class SimilarityParamsMapper : (VacancyDetailsDto) -> SimilarityParams {

    override fun invoke(dto: VacancyDetailsDto): SimilarityParams {
        return SimilarityParams(
            vacancyId = dto.vacancyId,
            profRoles = getProfRoles(dto.profRoles)
        )
    }

    private fun getProfRoles(profRoles: List<VacancyDetailsDto.ProfRole>?): List<String> {
        return profRoles?.map {
            it.profRoleId
        } ?: emptyList()
    }

}