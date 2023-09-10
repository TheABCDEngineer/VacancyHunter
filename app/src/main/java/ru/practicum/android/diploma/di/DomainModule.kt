package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.features.favorites.domain.FavoritesInteractor
import ru.practicum.android.diploma.features.favorites.domain.FavoritesInteractorImpl
import ru.practicum.android.diploma.features.similarvacancies.domain.SimilarVacanciesInteractor
import ru.practicum.android.diploma.features.similarvacancies.domain.SimilarVacanciesInteractorImpl
import ru.practicum.android.diploma.features.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.features.filters.domain.FiltersInteractorImpl
import ru.practicum.android.diploma.features.vacancydetails.domain.SharingInteractor
import ru.practicum.android.diploma.features.vacancydetails.domain.SharingInteractorImpl
import ru.practicum.android.diploma.features.vacancydetails.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.features.vacancydetails.domain.VacancyDetailsInteractorImpl

val domainModule = module {

    single<SharingInteractor> {
        SharingInteractorImpl()
    }

    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(vacancyRepository = get())
    }

    single<SimilarVacanciesInteractor> {
        SimilarVacanciesInteractorImpl(vacancyRepository = get())
    }
    
    single<FiltersInteractor> {
        FiltersInteractorImpl(get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(favoritesRepository = get())
    }

}