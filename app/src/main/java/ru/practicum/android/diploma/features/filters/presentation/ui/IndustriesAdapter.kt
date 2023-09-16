package ru.practicum.android.diploma.features.filters.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.filters.domain.models.Industry

class IndustriesAdapter: RecyclerView.Adapter<IndustriesViewHolder>() {
    var industries = mutableListOf<Industry>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_industry_list_view, parent, false)
        return IndustriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    override fun onBindViewHolder(holder: IndustriesViewHolder, position: Int) {
        holder.bind(industries[position])
    }

}