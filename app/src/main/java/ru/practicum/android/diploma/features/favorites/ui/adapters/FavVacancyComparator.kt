package ru.practicum.android.diploma.features.favorites.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiModel

object FavVacancyComparator : DiffUtil.ItemCallback<VacancyShortUiModel>() {
    override fun areItemsTheSame(oldItem: VacancyShortUiModel, newItem: VacancyShortUiModel): Boolean {
        // Id is unique.
        return oldItem.vacancyId == newItem.vacancyId
    }

    override fun areContentsTheSame(oldItem: VacancyShortUiModel, newItem: VacancyShortUiModel): Boolean {
        return oldItem == newItem
    }
}