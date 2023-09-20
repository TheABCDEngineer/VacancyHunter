package ru.practicum.android.diploma.features.search.presentation.ui.recyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.root.presentation.model.VacancyScreenModel

class VacancyViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    private val employerArtworkView: ImageView = itemView.findViewById(R.id.employer_artwork)
    private val jobTitleView: TextView = itemView.findViewById(R.id.job_title)
    private val vacancyDetailsView: TextView = itemView.findViewById(R.id.vacancy_details)

    fun bind(model: VacancyScreenModel) {
        Glide.with(itemView)
            .load(model.artworkUrl)
            .centerCrop()
            .placeholder(R.drawable.employer_placeholder)
            .into(employerArtworkView)

        jobTitleView.text = model.job
        vacancyDetailsView.text = model.details
    }
}