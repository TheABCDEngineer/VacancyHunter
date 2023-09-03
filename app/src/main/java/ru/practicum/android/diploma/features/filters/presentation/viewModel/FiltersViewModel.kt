package ru.practicum.android.diploma.features.filters.presentation.viewModel

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.features.filters.domain.models.Industry

class FiltersViewModel: ViewModel() {
    var industriesFullList: MutableList<Industry> = mutableListOf()

    fun getIndustries(): MutableList<Industry> {
        return industriesFullList
    }

    fun filterIndustries(text: String): MutableList<Industry> {
        if (text.isEmpty())
            return industriesFullList

        val temp = mutableListOf<Industry>()
        for (item in industriesFullList) {
            if (item.name.toLowerCase().contains(text.toLowerCase()))
                temp.add(item)
        }

        return temp
    }

}