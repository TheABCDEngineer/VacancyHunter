package ru.practicum.android.diploma.features.vacancydetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.features.favorites.domain.FavoritesInteractor
import ru.practicum.android.diploma.features.vacancydetails.domain.SharingInteractor
import ru.practicum.android.diploma.features.vacancydetails.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsEvent
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsState
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsUiMapper
import ru.practicum.android.diploma.root.domain.model.Outcome
import ru.practicum.android.diploma.util.debounce

class VacancyDetailsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val vacancyDetailsInteractor: VacancyDetailsInteractor,
    private val vacancyDetailsUiMapper: VacancyDetailsUiMapper,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<VacancyDetailsState>()
    val screenState: LiveData<VacancyDetailsState> get() = _screenState

    private val _externalNavEvent = MutableLiveData<Event<VacancyDetailsEvent>>()
    val externalNavEvent: LiveData<Event<VacancyDetailsEvent>> get() = _externalNavEvent

    private val toggleFavoriteDebounce =
        debounce(FAV_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            executeToggleFavorite()
        }

    private lateinit var domainModel: VacancyDetails

    fun getVacancyById(id: String) {
        if (id.isEmpty()) return

        viewModelScope.launch {

            _screenState.postValue(VacancyDetailsState.Loading)

            val result = vacancyDetailsInteractor.getVacancyById(id)
            when (result) {
                is Outcome.Success -> {
                    result.data?.let {
                        domainModel = it
                        _screenState.postValue(VacancyDetailsState.Content(vacancyDetailsUiMapper(it)))
                    }
                }

                else -> {
                    _screenState.postValue(VacancyDetailsState.Error)
                }
            }
        }
    }

    fun composeEmail() {
        val email = sharingInteractor.createEmailObject(
            domainModel.contactsEmail,
            domainModel.vacancyName
        )
        email?.let {
            _externalNavEvent.postValue(
                Event(VacancyDetailsEvent.ComposeEmail(email))
            )
        }
    }

    fun generateShareText(salary: String, address: String) {
        val strings = listOf(
            domainModel.vacancyName,
            salary,
            domainModel.employerName,
            address,
            domainModel.shareVacancyUrl)
        val message = sharingInteractor.generateShareText(strings)
        _externalNavEvent.postValue(
            Event(VacancyDetailsEvent.ShareVacancy(message))
        )
    }

    fun toggleFavorites() {
        toggleFavoriteDebounce()
    }

    private fun executeToggleFavorite() {
        viewModelScope.launch {
            val isFavorite = favoritesInteractor.toggleFavorites(domainModel)
            domainModel = domainModel.copy(isFavorite = isFavorite)
            _screenState.postValue(VacancyDetailsState.ToggleFavorite(isFavorite))
        }
    }

    companion object {
        private const val FAV_DEBOUNCE_DELAY_MILLIS = 300L
    }
}