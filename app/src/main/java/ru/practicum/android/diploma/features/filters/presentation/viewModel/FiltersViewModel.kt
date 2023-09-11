package ru.practicum.android.diploma.features.filters.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.features.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.features.filters.presentation.models.IndustryScreenState
import ru.practicum.android.diploma.root.domain.model.Outcome
import java.util.Locale

class FiltersViewModel(private val filtersInteractor: FiltersInteractor): ViewModel() {
    lateinit var industriesFullList: List<Industry>

    private val _industriesScreenState = MutableLiveData<IndustryScreenState>()
    val industriesScreenState: LiveData<IndustryScreenState> get() = _industriesScreenState

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
            val result = filtersInteractor.getCountries()
            when (result) {
                is Outcome.Success -> {
                    result.data?.let {
                        Log.d("test", "success ${it.size}")
                        //industriesFullList = it

                        //_industriesScreenState.postValue(IndustryScreenState.Content(it))
                    }
                }
                else -> {
                    Log.d("test", "error")
                    //_industriesScreenState.postValue(IndustryScreenState.Error)
                }
            }
        }
    }


}