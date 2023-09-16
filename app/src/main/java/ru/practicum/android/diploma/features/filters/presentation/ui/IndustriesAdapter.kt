package ru.practicum.android.diploma.features.filters.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.filters.domain.models.Industry

class IndustriesAdapter: RecyclerView.Adapter<IndustriesViewHolder>() {
    var industries = mutableListOf<Industry>()
    var onItemClick: ((Industry) -> Unit)? = null
    private var lastCheckedRadioButton: RadioButton? = null
    private var checkedIndustry: Industry? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_radio_button_list_view, parent, false)
        return IndustriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    override fun onBindViewHolder(holder: IndustriesViewHolder, position: Int) {
        if (position == 0 && checkedIndustry != null) {
            holder.bind(industries[position], true)
            lastCheckedRadioButton = holder.itemView.findViewById<RadioButton>(R.id.filter_radioButton)
        } else
            holder.bind(industries[position])

        val radioButton = holder.itemView.findViewById<RadioButton>(R.id.filter_radioButton)
        radioButton.setOnClickListener {
            onItemClick?.invoke(industries[position])
            lastCheckedRadioButton?.isChecked = false
            checkedIndustry = industries[position]
            lastCheckedRadioButton = radioButton
        }
    }

    fun getCheckedIndustry(): Industry? = checkedIndustry

    fun reload() {
        industries.clear()
        lastCheckedRadioButton = null
        notifyDataSetChanged()
    }

    fun clearCheckedIndustry() {
        checkedIndustry = null
    }

}