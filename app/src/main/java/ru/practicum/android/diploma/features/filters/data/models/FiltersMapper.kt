package ru.practicum.android.diploma.features.filters.data.models

import android.util.Log
import ru.practicum.android.diploma.features.filters.data.dto.AreaDto
import ru.practicum.android.diploma.features.filters.data.dto.IndustryDto
import ru.practicum.android.diploma.features.filters.data.dto.SubindustryDto
import ru.practicum.android.diploma.features.filters.domain.models.Area
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

    fun convertAreaTreeByParentIdDto(areasDto: List<AreaDto>, parentId: Int?): List<Area> {
        val areas = mutableListOf<Area>()
        for (areaDto in areasDto) {
            if (areaDto.id == parentId) {
                areas.addAll(convertAreaTreeBranch(areaDto.areas, convertAreaDto(areaDto)))
            }
        }
        return areas
    }

    fun convertAreaTreeDto(areasDto: List<AreaDto>): List<Area> {
        val areas = mutableListOf<Area>()
        for (areaDto in areasDto) {
            areas.addAll(convertAreaTreeBranch(areaDto.areas, convertAreaDto(areaDto)))
        }
        return areas
    }

    fun convertAreaTreeFirstLevelDto(areasDto: List<AreaDto>): List<Area> {
        val areas = mutableListOf<Area>()
        for (areaDto in areasDto) {
            areas.add(convertAreaDto(areaDto))
        }
        return areas
    }

    private fun convertAreaTreeBranch(areasDto: List<AreaDto>, country: Area?): List<Area> {
        val areas = mutableListOf<Area>()
        for (areaDto in areasDto) {
            if (areaDto.areas.isEmpty())
                areas.add(convertAreaDto(areaDto, country))
            else
                areas.addAll(convertAreaTreeBranch(areaDto.areas, country))
        }
        return areas
    }

    private fun convertAreaDto(areaDto: AreaDto, country: Area? = null) = Area(
        areaDto.id,
        areaDto.name,
        areaDto.parentId,
        country
    )
}