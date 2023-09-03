package ru.practicum.android.diploma.features.filters.data.models

import ru.practicum.android.diploma.features.filters.data.dto.IndustryDto
import ru.practicum.android.diploma.features.filters.data.dto.SubindustryDto
import ru.practicum.android.diploma.features.filters.domain.models.Industry

class FiltersMapper {
    fun convertSubIndustryDto(subindustryDto: SubindustryDto) = Industry(
        id = subindustryDto.id,
        name = subindustryDto.name
    )

    fun getIndustriesFromSubindustries(industriesDto: List<IndustryDto>): List<Industry> {
        var industries = mutableListOf<Industry>()
        for (industryDto in industriesDto) {
            for (subindustryDto in industryDto.subIndustries) {
                industries.add(convertSubIndustryDto(subindustryDto))
            }
        }
        return industries.sortedWith(compareBy({it.name}))
    }
}