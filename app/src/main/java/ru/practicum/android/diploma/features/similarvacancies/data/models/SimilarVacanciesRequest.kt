package ru.practicum.android.diploma.features.similarvacancies.data.models

import ru.practicum.android.diploma.features.similarvacancies.domain.models.SimilarityParams
import ru.practicum.android.diploma.root.data.network.models.Request

data class SimilarVacanciesRequest(val params: SimilarityParams): Request()