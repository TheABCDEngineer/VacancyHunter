package ru.practicum.android.diploma.root.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ListItemVacancyBinding
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiModel

class VacanciesAdapter(
    private val clickListener: VacancyClickListener
) : RecyclerView.Adapter<ShortVacancyViewHolder>() {

    private val vacancies = ArrayList<VacancyShortUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortVacancyViewHolder {

        val view = LayoutInflater.from(parent.context)

        return ShortVacancyViewHolder(
            ListItemVacancyBinding.inflate(view, parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: ShortVacancyViewHolder, position: Int) {
        holder.bind(vacancies[position])
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    fun updateAdapter(newVacancyList: List<VacancyShortUiModel>) {
        vacancies.clear()
        vacancies.addAll(newVacancyList)
        notifyDataSetChanged()
    }
}

