package ru.practicum.android.diploma.features.search.domain

import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.features.search.domain.interactor.VacancyFactoryInteractor
import ru.practicum.android.diploma.features.search.domain.repository.SearchVacancyRepository
import ru.practicum.android.diploma.features.search.presentation.ui.model.VacancyFactoryModel
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.domain.model.Outcome
import ru.practicum.android.diploma.root.domain.repository.FilterStorage
import kotlin.reflect.full.memberProperties

class VacancyFactory(
    private val vacancyRepository: SearchVacancyRepository,
    private val storage: FilterStorage
) : VacancyFactoryInteractor {
    private var searchingVacancyTitle = ""
    private var pagesCounter = 0
    private var pagesCount = 0
    private var vacancyCount = 0
    private lateinit var filter: Filter
    private val emptyFilter get() =
        Filter(null,null, null,null,false)

    override suspend fun getFirstVacancyPage(vacancyTitle: String): Outcome<VacancyFactoryModel> {
        searchingVacancyTitle = vacancyTitle
        pagesCounter = 0
        pagesCount = 0
        vacancyCount = 0
        filter = storage.getFilter() ?: emptyFilter

        return loadVacancies(isNewSearching = true)
    }

    override suspend fun getNextVacancyPage(): Outcome<VacancyFactoryModel> {
        pagesCounter += 1
        return loadVacancies(isNewSearching = false)
    }

    override fun getVacancyCount() = vacancyCount

    override fun getFilterRequirementsCount(): Int {
        var count = 0
        val _filter = storage.getFilter() ?: return 0
        for (property in Filter::class.memberProperties) {
            if (property.get(_filter) != null || property.get(_filter) == true) {
                count += 1
            }
        }
        return count
    }

    override fun getFilterHash(): Int? {
        val _filter = storage.getFilter() ?: return null
        return _filter.hashCode()
    }

    private suspend fun loadVacancies(isNewSearching: Boolean): Outcome<VacancyFactoryModel> {
        val response = vacancyRepository.loadVacancies(
            searchingVacancyTitle, filter, PER_PAGE, pagesCounter
        )

        when (response.status) {
            NetworkResultCode.SUCCESS -> {
                if (response.data == null) return Outcome.Error(status = NetworkResultCode.SERVER_ERROR)
                val data = response.data

                pagesCount =
                    if (data.pagesCount > MAX_PAGE + 1) MAX_PAGE + 1 else data.pagesCount
                vacancyCount =
                    if (data.foundCount > MAX_VACANCY_COUNT) MAX_VACANCY_COUNT else data.foundCount

                val vacancyFactoryModel = VacancyFactoryModel(
                    isNewSearching = isNewSearching,
                    items = data.resultVacancyList,
                    isContinueLoading = pagesCounter < pagesCount - 1
                )

                return Outcome.Success(vacancyFactoryModel)
            }

            else -> {
                return Outcome.Error(status = response.status)
            }
        }
    }

    companion object {
        const val MAX_VACANCY_COUNT = 2000
        const val PER_PAGE = 20
        const val MAX_PAGE = 99
    }
}