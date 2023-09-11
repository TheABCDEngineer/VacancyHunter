package ru.practicum.android.diploma.features.search.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.search.domain.interactor.VacancyFactoryInteractor
import ru.practicum.android.diploma.features.search.presentation.screenState.FilterState
import ru.practicum.android.diploma.features.search.presentation.screenState.SearchScreenState
import ru.practicum.android.diploma.features.search.presentation.screenState.SearchingCleanerState
import ru.practicum.android.diploma.features.search.presentation.ui.model.VacancyFactoryModel
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.domain.model.Outcome

class SearchViewModel(
    private val vacancyFactory: VacancyFactoryInteractor,
    private val context: Context
) : ViewModel() {
    private var previousSearchingRequest = ""
    private var previousFilterHash = vacancyFactory.getFilterHash()
    private var searchJob: Job? = null

    private val searchingCleanerState = MutableLiveData<SearchingCleanerState>()
    fun searchingCleanerStateObserve(): LiveData<SearchingCleanerState> = searchingCleanerState

    private val searchScreenState = MutableLiveData<SearchScreenState>()
    fun searchScreenStateObserve(): LiveData<SearchScreenState> = searchScreenState

    private val vacancyFeed = MutableLiveData<VacancyFactoryModel>()
    fun vacancyFeedObserve(): LiveData<VacancyFactoryModel> = vacancyFeed

    private val chipMessage = MutableLiveData<String>()
    fun chipMessageObserve(): LiveData<String> = chipMessage

    private val filterState = MutableLiveData<FilterState>()
    fun filterStateObserve(): LiveData<FilterState> = filterState

    fun onUiResume() {
        val currentFilterHash = vacancyFactory.getFilterHash()
        if (currentFilterHash == previousFilterHash) return
        previousFilterHash = currentFilterHash

        val count = vacancyFactory.getFilterRequirementsCount()
        val state =
            if (count > 0) FilterState.Active(count) else FilterState.Inactive()
        filterState.postValue(state)

        if (previousSearchingRequest.isNotEmpty()) runSearching(delay = 0L) {
            searchScreenState.postValue(SearchScreenState.SEARCHING)
            vacancyFactory.getFirstVacancyPage(previousSearchingRequest)
        }
    }

    fun onSearchingRequestChange(text: String?) {
        val cleanerState =
            if (text.isNullOrEmpty()) SearchingCleanerState.INACTIVE else SearchingCleanerState.ACTIVE
        searchingCleanerState.postValue(cleanerState)

        if (text == previousSearchingRequest) return
        previousSearchingRequest = text ?: ""

        if (text.isNullOrEmpty()) {
            searchJob?.cancel()
            return
        }

        runSearching(delay = CLICK_DEBOUNCE_DELAY_MILLIS) {
            searchScreenState.postValue(SearchScreenState.SEARCHING)
            vacancyFactory.getFirstVacancyPage(text)
        }
    }

    fun getNextSearchingPage() {
        runSearching(delay = 0L) {
            vacancyFactory.getNextVacancyPage()
        }
    }

    fun onSearchingFieldClean() {
        searchScreenState.postValue(SearchScreenState.NEWBORN)
        searchingCleanerState.postValue(SearchingCleanerState.INACTIVE)
    }

    private fun runSearching(delay: Long, action: suspend() -> Outcome<VacancyFactoryModel>) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(delay)
            handleSearchingResponse(
                action.invoke()
            )
            searchJob = null
        }
    }

    private fun handleSearchingResponse(response: Outcome<VacancyFactoryModel>) {
        when (response.status) {
            NetworkResultCode.SUCCESS -> {
                if (response.data == null) {
                    provideScreenState(NetworkResultCode.SERVER_ERROR)
                    return
                }
                val data = response.data!!
                provideScreenState(NetworkResultCode.SUCCESS, data)
                if (data.items.isEmpty() && data.isNewSearching) return
                vacancyFeed.postValue(data)
            }

            else -> provideScreenState(response.status)
        }
    }

    private fun provideScreenState(networkResultCode: NetworkResultCode, model: VacancyFactoryModel? = null) {
        var screenState = SearchScreenState.RESPONSE_RESULTS
        var chipMessage = ""

        when (networkResultCode) {
            NetworkResultCode.SUCCESS -> {
                if (model ==  null) return
                chipMessage =
                    context.getString(R.string.found) + " " + modifyToStringVacancyQuantity(
                        vacancyFactory.getVacancyCount()
                    )
                screenState = SearchScreenState.RESPONSE_RESULTS

                if (model.items.isEmpty() && model.isNewSearching) {
                    screenState = SearchScreenState.EMPTY_RESULT
                    chipMessage = context.getString(R.string.no_such_vacancies)
                }
            }
            NetworkResultCode.SERVER_ERROR -> {
                screenState = SearchScreenState.SOMETHING_WENT_WRONG
                chipMessage = context.getString(R.string.server_error)
            }
            NetworkResultCode.CONNECTION_ERROR -> {
                screenState = SearchScreenState.SOMETHING_WENT_WRONG
                chipMessage = context.getString(R.string.no_internet_connection)
            }
            NetworkResultCode.UNKNOWN_ERROR -> {
                screenState = SearchScreenState.SOMETHING_WENT_WRONG
                chipMessage = context.getString(R.string.something_went_wrong)
            }
        }

        this.searchScreenState.postValue(screenState)
        this.chipMessage.postValue(chipMessage)
    }

    private fun modifyToStringVacancyQuantity(quantity: Int): String {
        val ending = when (quantity.toString().takeLast(1).toInt()) {
            1 -> context.getString(R.string.vacancy_one)
            2, 3, 4 -> context.getString(R.string.vacancy_2_3_4)
            else -> context.getString(R.string.vacancy_many)
        }
        return "$quantity $ending"
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}