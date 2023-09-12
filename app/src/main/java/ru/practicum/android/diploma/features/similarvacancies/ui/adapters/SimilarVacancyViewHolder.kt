package ru.practicum.android.diploma.features.similarvacancies.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ListItemVacancySimilarBinding
import ru.practicum.android.diploma.features.similarvacancies.presentation.models.VacancySimilarShortUiModel

class SimilarVacancyViewHolder(
    private val binding: ListItemVacancySimilarBinding,
    private val clickListener: SimilarVacanciesAdapter.ListItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: VacancySimilarShortUiModel) {

        binding.apply {
            cardTitle.text = vacancy.cardTitle
            employerName.text = vacancy.employerName
            salary.text = vacancy.salary
        }

        Glide.with(binding.logoImage)
            .load(vacancy.logoUrl)
            .centerInside()
            .transform(RoundedCorners(dpToPx()))
            .placeholder(R.drawable.padded_logo_placeholder)
            .into(binding.logoImage)

        itemView.setOnClickListener {
            clickListener.onListItemClick(vacancy = vacancy)
        }
    }

    private fun dpToPx(): Int {
        val density = this.itemView.context.resources.displayMetrics.density
        return (LOGO_CORNER_RADIUS_DP * density).toInt()
    }

    companion object {
        private const val LOGO_CORNER_RADIUS_DP = 12
    }
}