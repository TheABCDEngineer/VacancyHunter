package ru.practicum.android.diploma.features.filters.presentation.ui

import android.view.View
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.filters.domain.models.Area

class RegionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
    private val regionRB: RadioButton = itemView.findViewById(R.id.filter_radioButton)

    fun bind(region: Area) {
        regionRB.text = region.name
        regionRB.isChecked = false
    }
}