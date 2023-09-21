package ru.practicum.android.diploma.features.favorites.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.databinding.ListItemVacancyBinding
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiModel
import ru.practicum.android.diploma.root.presentation.ui.adapters.ShortVacancyViewHolder
import ru.practicum.android.diploma.root.presentation.ui.adapters.VacancyClickListener

class PagedFavoritesAdapter(
    private val clickListener: VacancyClickListener,
    diffCallback: DiffUtil.ItemCallback<VacancyShortUiModel>
) : PagingDataAdapter<VacancyShortUiModel, ShortVacancyViewHolder>(diffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShortVacancyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ShortVacancyViewHolder(
            ListItemVacancyBinding.inflate(layoutInflater, parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: ShortVacancyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let{
            holder.bind(item)
        }
    }
}


