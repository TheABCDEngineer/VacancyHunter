package ru.practicum.android.diploma.root.presentation.ui

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary
import java.text.NumberFormat
import java.util.Locale

object SalaryFormat {
    fun formatSalaryString(salaryObj: Salary?, context: Context): String {
        if (salaryObj == null) return ""
        val from = salaryObj.salaryLowerBoundary
        val to = salaryObj.salaryUpperBoundary
        val currencySymbol = getCurrencySymbol(salaryObj.salaryCurrency)

        return when {

            from != null && to != null -> {
                String.format(
                    context.getString(R.string.salary_from_to),
                    formatNumber(from),
                    formatNumber(to),
                    currencySymbol
                )
            }

            from != null && to == null -> {
                String.format(
                    context.getString(R.string.salary_from),
                    formatNumber(from),
                    currencySymbol
                )
            }

            from == null && to != null -> {
                String.format(
                    context.getString(R.string.salary_to),
                    formatNumber(to),
                    currencySymbol
                )
            }

            else -> context.getString(R.string.no_salary)
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