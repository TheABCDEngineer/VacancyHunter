package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.features.similarvacancies.domain.SimilarVacanciesInteractor
import ru.practicum.android.diploma.features.similarvacancies.domain.SimilarVacanciesInteractorImpl
import ru.practicum.android.diploma.features.vacancydetails.domain.SharingInteractor
import ru.practicum.android.diploma.features.vacancydetails.domain.SharingInteractorImpl
import ru.practicum.android.diploma.features.vacancydetails.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.features.vacancydetails.domain.VacancyDetailsInteractorImpl

val domainModule = module {

    factory<SharingInteractor> {
        SharingInteractorImpl()
    }

    factory<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(vacancyRepository = get())
    }

    factory<SimilarVacanciesInteractor> {
        SimilarVacanciesInteractorImpl(vacancyRepository = get())
    }

}