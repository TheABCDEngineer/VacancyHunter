package ru.practicum.android.diploma.features.similarvacancies.ui.adapters

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ListItemVacancySimilarBinding
import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary


class SimilarVacancyViewHolder(
    private val binding: ListItemVacancySimilarBinding,
    private val clickListener: SimilarVacanciesAdapter.ListItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: VacancyShortSimilar) {

        binding.cardTitle.text = formatTitle(vacancy.vacancyName, vacancy.employerArea)

        binding.employerName.text = vacancy.employerName

        vacancy.salary?.let {
            binding.salary.text = formatSalaryString(it)
        }

        Glide.with(binding.logoImage)
            .load(vacancy.logoUrl)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(R.dimen.logo_corner_radius)))
            .placeholder(R.drawable.placeholder)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.logoImage.setPadding(
                        R.dimen.logo_padding_with_image,
                        R.dimen.logo_padding_with_image,
                        R.dimen.logo_padding_with_image,
                        R.dimen.logo_padding_with_image
                    )
                    return false
                }
            }
            )
            .into(binding.logoImage)


        itemView.setOnClickListener {
            clickListener.onListItemClick(vacancy = vacancy)
        }
    }

    private fun formatTitle(title: String, area: String): String {
        val list = listOf(title, area).map { it.isNotEmpty() }
        return list.joinToString { ", " }
    }

    private fun dpToPx(dp: Int): Int {
        val density = this.itemView.context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun formatSalaryString(salaryObj: Salary?): String {
        if (salaryObj == null) return ""
        val from = salaryObj.salaryLowerBoundary
        val to = salaryObj.salaryUpperBoundary
        val currency = salaryObj.salaryCurrency

        when {
            currency.isNullOrEmpty() -> return ""

            from != null && to != null -> return String.format(
                this.itemView.context.getString(R.string.salary_from_to),
                from,
                to,
                currency
            )

            from != null && to == null -> return String.format(
                this.itemView.context.getString(R.string.salary_from),
                from,
                currency
            )

            from == null && to != null -> return String.format(
                this.itemView.context.getString(R.string.salary_to),
                to,
                currency
            )
        }

        return ""
    }

}