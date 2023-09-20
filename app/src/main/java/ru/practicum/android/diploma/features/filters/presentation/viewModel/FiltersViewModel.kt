package ru.practicum.android.diploma.features.filters.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.features.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.features.filters.domain.models.Area
import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.features.filters.presentation.models.CountryScreenState
import ru.practicum.android.diploma.features.filters.presentation.models.FilterScreenState
import ru.practicum.android.diploma.features.filters.presentation.models.IndustryScreenState
import ru.practicum.android.diploma.features.filters.presentation.models.RegionScreenState
import ru.practicum.android.diploma.root.presentation.Event
import ru.practicum.android.diploma.root.domain.model.Outcome
import java.util.Locale

class FiltersViewModel(
    private val filtersInteractor: FiltersInteractor
): ViewModel() {
    lateinit var industriesFullList: List<Industry>
    lateinit var regionsFullList: List<Area>

    private val _industriesScreenState = MutableLiveData<IndustryScreenState>()
    val industriesScreenState: LiveData<IndustryScreenState> get() = _industriesScreenState

    private val _countriesScreenState = MutableLiveData<CountryScreenState>()
    val countriesScreenState: LiveData<CountryScreenState> get() = _countriesScreenState

    private val _regionsScreenState = MutableLiveData<RegionScreenState>()
    val regionsScreenState: LiveData<RegionScreenState> get() = _regionsScreenState

    private val _mainScreenRenderEvent = MutableLiveData<Event<FilterScreenState>>()
    val mainScreenRenderEvent: LiveData<Event<FilterScreenState>> get() = _mainScreenRenderEvent

    private var country: Area? = null
    private var region: Area? = null

    private var filterCountry: Area? = null
    private var filterRegion: Area? = null
    private var filterIndustry: Industry? = null
    private var filterSalary: String? = null
    private var filterDoNotShowWithoutSalary: Boolean = false

    fun filterIndustries(text: String): List<Industry> {
        if (text.isEmpty())
            return industriesFullList

        val temp = mutableListOf<Industry>()
        for (item in industriesFullList) {
            if (item.name.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT)))
                temp.add(item)
        }

        return temp.toList()
    }

    fun filterRegions(text: String): List<Area> {
        if (text.isEmpty())
            return regionsFullList

        val temp = mutableListOf<Area>()
        for (item in regionsFullList) {
            if (item.name.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT)))
                temp.add(item)
        }

        return temp.toList()
    }

    fun getIndustries() {
        viewModelScope.launch {
            _industriesScreenState.postValue(IndustryScreenState.Loading)

            val result = filtersInteractor.getIndustries()
            when (result) {
                is Outcome.Success -> {
                    result.data?.let {
                        industriesFullList = it
                        _industriesScreenState.postValue(IndustryScreenState.Content(it))
                    }
                }
                else -> {
                    _industriesScreenState.postValue(IndustryScreenState.Error)
                }
            }
        }
    }

    fun getCountries() {
        viewModelScope.launch {
            _countriesScreenState.postValue(CountryScreenState.Loading)
            val result = filtersInteractor.getCountries()
            when (result) {
                is Outcome.Success -> {
                    result.data?.let {
                        _countriesScreenState.postValue(CountryScreenState.Content(it))
                    }
                }
                else -> {
                    _countriesScreenState.postValue(CountryScreenState.Error)
                }
            }
        }
    }

    fun getRegions() {
        viewModelScope.launch {
            _regionsScreenState.postValue(RegionScreenState.Loading)
            val result = filtersInteractor.getRegions(country?.id)
            when (result) {
                is Outcome.Success -> {
                    result.data?.let {
                        regionsFullList = it
                        _regionsScreenState.postValue(RegionScreenState.Content(it))
                    }
                }
                else -> {
                    _regionsScreenState.postValue(RegionScreenState.Error)
                }
            }
        }
    }

    fun setRegion(chosenRegion: Area?) {
        region = chosenRegion
        if (region != null)
            country = chosenRegion?.country
    }

    fun getRegion() = region

    fun setCountry(chosenCountry: Area?) {
        country = chosenCountry
        if (country != region?.country)
            region = null
    }

    fun getCountry() = country

    fun setFilterCountry() {
        filterCountry = country
    }

    fun setFilterRegion() {
        filterRegion = region
    }

    fun setFilterIndustry(industry: Industry?) {
        filterIndustry = industry
    }
    fun setFilterSalary(salary: String?) {
        filterSalary = salary
    }

    fun setFilterDoNotShowWithoutSalary(value: Boolean) {
        filterDoNotShowWithoutSalary = value
    }

    fun getWorkPlace(): String {
        return if (filterCountry == null && filterRegion == null)
            ""
        else if (filterRegion == null)
            "${filterCountry?.name}"
        else
            "${filterCountry?.name}, ${filterRegion?.name}"
    }

    fun getIndustry() = filterIndustry

    fun getSalary() = filterSalary

    fun getDoNotShowWithoutSalary() = filterDoNotShowWithoutSalary

    fun clearWorkPlace() {
        setCountry(null)
        setRegion(null)
        setFilterCountry()
        setFilterRegion()
    }

    fun saveFilter() {
        val filter = Filter(
            industry = filterIndustry,
            country = filterCountry,
            region = filterRegion,
            salary = if (filterSalary.isNullOrEmpty()) null else filterSalary,
            filterDoNotShowWithoutSalary
        )
        filtersInteractor.saveFilter(filter)
    }

    fun clearFilter() {
        filterCountry = null
        filterRegion = null
        filterIndustry = null
        filterSalary = ""
        filterDoNotShowWithoutSalary = false
        saveFilter()
    }

    fun isFilterButtonsAvailable() = (
            filterCountry != null
                    || filterRegion != null
                    || filterIndustry != null
                    || !filterSalary.isNullOrEmpty()
                    || filterDoNotShowWithoutSalary
            )

    fun isWorkPlaceChooseButtonAvailable() = (country != null || region != null)

    fun getSavedFilters() {
        val filter = filtersInteractor.getSavedFilters()
        if (filter == null) return
        filterIndustry = filter.industry
        filterCountry = filter.country
        filterRegion = filter.region
        filterSalary = filter.salary
        filterDoNotShowWithoutSalary = filter.doNotShowWithoutSalary

        setCountry(filterCountry)
        setRegion(filterRegion)

        _mainScreenRenderEvent.postValue(Event(FilterScreenState.MainScreen))
    }
}