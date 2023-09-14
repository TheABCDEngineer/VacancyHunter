package ru.practicum.android.diploma.features.filters.data.models

import android.util.Log
import ru.practicum.android.diploma.features.filters.data.dto.AreaDto
import ru.practicum.android.diploma.features.filters.data.dto.CountryDto
import ru.practicum.android.diploma.features.filters.data.dto.IndustryDto
import ru.practicum.android.diploma.features.filters.data.dto.SubindustryDto
import ru.practicum.android.diploma.features.filters.domain.models.Area
import ru.practicum.android.diploma.features.filters.domain.models.Country
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

    fun convertCountryDto(countryDto: CountryDto) = Country(
        countryDto.id,
        countryDto.name
    )

    fun convertAreaTreeDto(areasDto: List<AreaDto>): List<Area> {
        val areas = mutableListOf<Area>()
        areas.addAll(convertAreaTreeBranch(areasDto))
        return areas
    }

    fun convertAreaTreeByParentIdDto(areasDto: List<AreaDto>, parentId: Int?): List<Area> {
        val areas = mutableListOf<Area>()
        for (areaDto in areasDto) {
            Log.d("test", "3.. -- ${areaDto.parentId} -- $parentId")
            if (areaDto.id == parentId) {
                Log.d("test", "regions -- ${areaDto.parentId}")
                areas.addAll(convertAreaTreeBranch(areaDto.areas))
            }
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

    private fun convertAreaTreeBranch(areasDto: List<AreaDto>): List<Area> {
        val areas = mutableListOf<Area>()
        for (areaDto in areasDto) {
            areas.add(convertAreaDto(areaDto))
            areas.addAll(convertAreaTreeBranch(areaDto.areas))
        }
        return areas
    }

    private fun convertAreaTreeBranchByParentId(areasDto: List<AreaDto>, parentId: Int?): List<Area> {
        val areas = mutableListOf<Area>()
        for (areaDto in areasDto) {
            Log.d("test", "2 regions ${areaDto.parentId}")
            areas.add(convertAreaDto(areaDto))
            areas.addAll(convertAreaTreeBranchByParentId(areaDto.areas, parentId))
        }
        return areas
    }

    private fun convertAreaDto(areaDto: AreaDto) = Area(
        areaDto.id,
        areaDto.name,
        areaDto.parentId
    )
}