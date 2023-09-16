package ru.practicum.android.diploma.features.filters.data.dto

import com.google.gson.annotations.SerializedName

data class IndustryDto(
    val id: String,
    val name: String,
    @SerializedName("industries") val subIndustries: List<SubindustryDto>
)