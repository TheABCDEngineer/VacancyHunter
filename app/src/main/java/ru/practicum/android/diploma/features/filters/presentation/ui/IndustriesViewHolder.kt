package ru.practicum.android.diploma.features.filters.presentation.ui

import android.view.View
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.filters.domain.models.Industry

class IndustriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val industryRB: RadioButton = itemView.findViewById(R.id.filter_radioButton)

    fun bind(industry: Industry, isChecked: Boolean = false) {
        industryRB.text = industry.name
        industryRB.isChecked = isChecked
    }
}