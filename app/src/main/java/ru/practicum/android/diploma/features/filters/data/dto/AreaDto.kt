package ru.practicum.android.diploma.features.filters.data.dto

data class AreaDto(
    val id: Int,
    val name: String,
    val parentId: Int?,
    val areas: List<AreaDto>
)