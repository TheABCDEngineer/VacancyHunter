package ru.practicum.android.diploma.features.similarvacancies.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ListItemVacancySimilarBinding
import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary
import ru.practicum.android.diploma.root.presentation.ui.CurrencySymbol
import java.text.NumberFormat
import java.util.Locale


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
            .centerInside()
            .transform(RoundedCorners(dpToPx(R.dimen.logo_corner_radius)))
            .placeholder(R.drawable.padded_logo_placeholder)
            .into(binding.logoImage)



        itemView.setOnClickListener {
            clickListener.onListItemClick(vacancy = vacancy)
        }
    }

    private fun formatTitle(title: String, area: String): String {
        val list = listOf(title, area).filter { it.isNotEmpty() }
        return list.joinToString(", ")
    }

    private fun dpToPx(dp: Int): Int {
        val density = this.itemView.context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun formatSalaryString(salaryObj: Salary?): String {
        if (salaryObj == null) return ""
        val from = salaryObj.salaryLowerBoundary
        val to = salaryObj.salaryUpperBoundary
        val currencySymbol = getCurrencySymbol(salaryObj.salaryCurrency)

        return when {

            from != null && to != null -> {
                String.format(
                    this.itemView.context.getString(R.string.salary_from_to),
                    formatNumber(from),
                    formatNumber(to),
                    currencySymbol
                )
            }

            from != null && to == null -> {
                String.format(
                    this.itemView.context.getString(R.string.salary_from),
                    formatNumber(from),
                    currencySymbol
                )
            }

            from == null && to != null -> {
                String.format(
                    this.itemView.context.getString(R.string.salary_to),
                    formatNumber(to),
                    currencySymbol
                )
            }

            else -> this.itemView.context.getString(R.string.no_salary)
        }
    }

    private fun getCurrencySymbol(currencyStr: String?): String {
        return currencyStr?.let {
            CurrencySymbol.getCurrencySymbol(it)
        } ?: ""
    }

    private fun formatNumber(number: Int): String {
        val formatInstance = NumberFormat.getNumberInstance(Locale.getDefault())
        return formatInstance.format(number)
    }
}