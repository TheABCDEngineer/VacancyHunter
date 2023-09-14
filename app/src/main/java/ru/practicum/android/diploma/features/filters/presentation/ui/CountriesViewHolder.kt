package ru.practicum.android.diploma.features.filters.presentation.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.filters.domain.models.Area

class CountriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(country: Area) {
        itemView.findViewById<TextView>(R.id.filter_country_name).text = country.name
    }
}