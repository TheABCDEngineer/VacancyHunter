package ru.practicum.android.diploma.features.search.domain.interactor

import ru.practicum.android.diploma.features.search.presentation.ui.model.VacancyFactoryModel
import ru.practicum.android.diploma.root.domain.model.Outcome

interface VacancyFactoryInteractor {
    suspend fun getFirstVacancyPage(vacancyTitle: String): Outcome<VacancyFactoryModel>
    suspend fun getNextVacancyPage(): Outcome<VacancyFactoryModel>
    fun getVacancyCount(): Int
    fun getFilterRequirementsCount(): Int
    fun getFilterHash(): Int?
}