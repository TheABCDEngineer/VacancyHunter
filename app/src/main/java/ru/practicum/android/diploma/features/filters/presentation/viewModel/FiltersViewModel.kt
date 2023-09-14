package ru.practicum.android.diploma.features.filters.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.features.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.features.filters.domain.models.Area
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.features.filters.presentation.models.CountryScreenState
import ru.practicum.android.diploma.features.filters.presentation.models.IndustryScreenState
import ru.practicum.android.diploma.root.domain.model.Outcome
import java.util.Locale

class FiltersViewModel(private val filtersInteractor: FiltersInteractor): ViewModel() {
    lateinit var industriesFullList: List<Industry>

    private val _industriesScreenState = MutableLiveData<IndustryScreenState>()
    val industriesScreenState: LiveData<IndustryScreenState> get() = _industriesScreenState

    private val _countriesScreenState = MutableLiveData<CountryScreenState>()
    val countriesScreenState: LiveData<CountryScreenState> get() = _countriesScreenState

    private var country: Area? = null
    private var region: Area? = null

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
            val result = filtersInteractor.getRegions(country?.parentId)
            when (result) {
                is Outcome.Success -> {
                    result.data?.let {
                        Log.d("test", "success regions ${it.size}")
                    }
                }
                else -> {
                    Log.d("test", "error regions")
                }
            }
        }
    }


}