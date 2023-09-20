package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.bind
import ru.practicum.android.diploma.features.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.features.vacancydetails.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.features.search.presentation.viewModel.SearchViewModel
import ru.practicum.android.diploma.features.vacancydetails.ui.ExternalNavigator
import ru.practicum.android.diploma.features.filters.presentation.viewModel.FiltersViewModel
import ru.practicum.android.diploma.features.similarvacancies.presentation.SimilarVacanciesViewModel
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiMapper
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsUiMapper

val uiModule = module {

    viewModel<VacancyDetailsViewModel> {
        VacancyDetailsViewModel(
            sharingInteractor = get(),
            vacancyDetailsInteractor = get(),
            vacancyDetailsUiMapper = get(),
            favoritesInteractor = get()
        )
    }
    
    viewModel<FiltersViewModel>{
        FiltersViewModel(get())
    }

    single<ExternalNavigator> {
        ExternalNavigator()
    }

    viewModelOf(::SearchViewModel).bind()

    viewModel<SimilarVacanciesViewModel> {
        SimilarVacanciesViewModel(interactor = get(), uiMapper = get())
    }

    viewModel<FavoritesViewModel>() {
        FavoritesViewModel(interactor = get(), uiMapper = get())
    }

    single<VacancyShortUiMapper> {
        VacancyShortUiMapper(context = androidContext())
    }

    single<VacancyDetailsUiMapper> {
        VacancyDetailsUiMapper(context = androidContext())
    }

}