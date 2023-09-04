package ru.practicum.android.diploma.features.search.data.network.dto

import com.google.gson.annotations.SerializedName

data class VacancyResponse(
    @SerializedName("items") val items: ArrayList<VacancyShortDto>
)
