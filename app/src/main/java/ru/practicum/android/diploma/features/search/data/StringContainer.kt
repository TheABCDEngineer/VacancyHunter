package ru.practicum.android.diploma.features.search.data

import android.content.Context
import ru.practicum.android.diploma.R

object StringContainer {
    var found = ""
        private set

    var no_such_vacancies = ""
        private set

    var server_error = ""
        private set

    var no_internet_connection = ""
        private set

    var something_went_wrong =""
        private set

    var vacancies = Vacancies()
        private set

    fun setValues(context: Context) {
        found = context.getString(R.string.found)
        no_such_vacancies = context.getString(R.string.no_such_vacancies)
        server_error = context.getString(R.string.server_error)
        no_internet_connection = context.getString(R.string.no_internet_connection)
        something_went_wrong = context.getString(R.string.something_went_wrong)

        vacancies = Vacancies(
            one = context.getString(R.string.vacancy_one),
            many = context.getString(R.string.vacancy_2_3_4),
            other = context.getString(R.string.vacancy_many)
        )
    }

    data class Vacancies(
        val one: String = "",
        val many: String = "",
        val other: String = ""
    )
}