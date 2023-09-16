package ru.practicum.android.diploma.features.favorites.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.databinding.ListItemVacancyBinding
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiModel

class PagedFavoritesAdapter(
    private val clickListener: ListItemClickListener,
    diffCallback: DiffUtil.ItemCallback<VacancyShortUiModel>
) : PagingDataAdapter<VacancyShortUiModel, FavoriteViewHolder>(diffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
        return FavoriteViewHolder(
            ListItemVacancyBinding.inflate(view, parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    interface ListItemClickListener {
        fun onListItemClick(vacancy: VacancyShortUiModel)
    }
}


