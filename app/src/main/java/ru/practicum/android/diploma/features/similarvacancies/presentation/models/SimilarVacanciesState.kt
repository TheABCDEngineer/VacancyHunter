package ru.practicum.android.diploma.features.similarvacancies.presentation.models

import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiModel

sealed class SimilarVacanciesState {

    object Loading : SimilarVacanciesState()
    object NothingFound : SimilarVacanciesState()

    data class Error(
        val errorCode: NetworkResultCode?
    ) : SimilarVacanciesState()

    data class Content(
        val similarVacancies: List<VacancyShortUiModel>,
    ) : SimilarVacanciesState()

}