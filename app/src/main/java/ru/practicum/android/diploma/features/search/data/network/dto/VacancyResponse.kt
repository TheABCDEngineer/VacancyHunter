package ru.practicum.android.diploma.features.search.data.network.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.root.data.network.models.Response

data class VacancyResponse(
    @SerializedName("items") val items: ArrayList<VacancyShortDto>,
    @SerializedName("found") val found: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("page") val page: Int
): Response
