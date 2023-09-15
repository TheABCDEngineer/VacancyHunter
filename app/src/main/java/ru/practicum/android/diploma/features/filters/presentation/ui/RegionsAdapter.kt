package ru.practicum.android.diploma.features.filters.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.filters.domain.models.Area

class RegionsAdapter: RecyclerView.Adapter<RegionsViewHolder>() {
    var regions = mutableListOf<Area>()
    var onItemClick: ((Area) -> Unit)? = null
    private var lastCheckedRadioButton: RadioButton? = null
    private var checkedRegion: Area? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_radio_button_list_view, parent, false)
        return RegionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return regions.size
    }

    override fun onBindViewHolder(holder: RegionsViewHolder, position: Int) {
        holder.bind(regions[position])

        val radioButton = holder.itemView.findViewById<RadioButton>(R.id.filter_radioButton)
        radioButton.setOnClickListener {
            onItemClick?.invoke(regions[position])
            lastCheckedRadioButton?.isChecked = false
            checkedRegion = regions[position]
            lastCheckedRadioButton = radioButton
        }
    }

    fun getCheckedArea(): Area? = checkedRegion

    fun reload() {
        regions.clear()
        lastCheckedRadioButton = null
        checkedRegion = null
        notifyDataSetChanged()
    }
}