package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.features.filters.data.models.FiltersMapper
import ru.practicum.android.diploma.features.search.data.SearchVacancyRepositoryImplNetwork
import ru.practicum.android.diploma.features.search.data.network.NetworkClient
import ru.practicum.android.diploma.features.search.data.network.NetworkClientImplRetrofit
import ru.practicum.android.diploma.features.search.domain.repository.SearchVacancyRepository
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarVacanciesMapper
import ru.practicum.android.diploma.features.similarvacancies.data.models.SimilarityParamsMapper
import ru.practicum.android.diploma.features.vacancydetails.data.models.VacancyDetailsMapper
import ru.practicum.android.diploma.root.data.DataConverter
import ru.practicum.android.diploma.root.data.FilterImplSharedPreference
import ru.practicum.android.diploma.root.data.StorageKeys
import ru.practicum.android.diploma.root.data.VacancyRepositoryImpl
import ru.practicum.android.diploma.root.data.network.HeadHunterApi
import ru.practicum.android.diploma.root.data.network.HeaderInterceptor
import ru.practicum.android.diploma.root.data.network.NetworkSearch
import ru.practicum.android.diploma.root.data.network.ResponseProcessor
import ru.practicum.android.diploma.root.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.root.domain.VacancyRepository
import ru.practicum.android.diploma.root.domain.repository.FilterRepository

val dataModule = module {

    single<HeaderInterceptor> {
        HeaderInterceptor()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HeaderInterceptor>())
            .build()
    }

    single<HeadHunterApi> {
        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .build()
            .create(HeadHunterApi::class.java)
    }

    single<NetworkSearch> {
        RetrofitNetworkClient(api = get(), context = androidContext())
    }

    single<Gson> {
        Gson()
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(StorageKeys.VACANCY_HUNTER_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<VacancyDetailsMapper>  {
        VacancyDetailsMapper()
    }

    singleOf(::SearchVacancyRepositoryImplNetwork).bind<SearchVacancyRepository>()

    single<SimilarityParamsMapper> {
        SimilarityParamsMapper()
    }

    single<SimilarVacanciesMapper> {
        SimilarVacanciesMapper()
    }

    single<ResponseProcessor> {
        ResponseProcessor()
    }

    single<VacancyRepository> {
        VacancyRepositoryImpl(
            detailsMapper = get(),
            simParamsMapper = get(),
            similarVacanciesMapper = get(),
            responseProcessor = get(),
            networkClient = get(),
            gson = get())
    }

    singleOf(::FilterImplSharedPreference).bind<FilterRepository>()

    singleOf(::DataConverter).bind()

    singleOf(::NetworkClientImplRetrofit).bind<NetworkClient>()
    
    single<FiltersMapper> {
        FiltersMapper()
    }

}