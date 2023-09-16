package ru.practicum.android.diploma.features.similarvacancies.data.models

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsDto

class SimilarVacanciesResponse(@SerializedName("items") val similarVacanciesList: List<VacancyDetailsDto>)