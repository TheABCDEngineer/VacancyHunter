package ru.practicum.android.diploma.features.filters.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.filters.domain.models.Area

class CountriesAdapter: RecyclerView.Adapter<CountriesViewHolder>() {
    var countries = mutableListOf<Area>()
    var onItemClick: ((Area) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_country_list_view, parent, false)
        return CountriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(countries[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(countries[position])
        }
    }
}