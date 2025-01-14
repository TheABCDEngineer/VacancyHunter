package ru.practicum.android.diploma.features.similarvacancies.data.models

import ru.practicum.android.diploma.features.similarvacancies.domain.models.SimilarityParams
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto
import ru.practicum.android.diploma.root.data.network.models.Response

class SimilarityParamsMapper : (Response) -> SimilarityParams {

    override fun invoke(dto: Response): SimilarityParams {
        dto as VacancyDetailsDto
        return SimilarityParams(
            vacancyId = dto.vacancyId,
            profRoles = getProfRoles(dto.profRoles)
        )
    }

    private fun getProfRoles(profRoles: List<VacancyDetailsDto.ProfRole>?): List<String>? {
        return profRoles?.map {
            it.profRoleId
        }
    }

}