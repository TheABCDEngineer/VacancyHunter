package ru.practicum.android.diploma.features.similarvacancies.data.models

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.root.data.network.models.Response
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto

data class SimilarVacanciesResponse(
    @SerializedName("items") val similarVacanciesList: List<VacancyDetailsDto>
): Response