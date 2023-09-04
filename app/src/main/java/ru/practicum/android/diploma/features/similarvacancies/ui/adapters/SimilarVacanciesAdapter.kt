package ru.practicum.android.diploma.features.similarvacancies.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ListItemVacancySimilarBinding
import ru.practicum.android.diploma.features.similarvacancies.presentation.models.VacancySimilarShortUiModel

class SimilarVacanciesAdapter(
    private val clickListener: ListItemClickListener
) : RecyclerView.Adapter<SimilarVacancyViewHolder>() {

    private val vacancies = ArrayList<VacancySimilarShortUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarVacancyViewHolder {

        val view = LayoutInflater.from(parent.context)

        return SimilarVacancyViewHolder(
            ListItemVacancySimilarBinding.inflate(view, parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: SimilarVacancyViewHolder, position: Int) {
        holder.bind(vacancies[position])
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    fun updateAdapter(newVacancyList: List<VacancySimilarShortUiModel>) {
        vacancies.clear()
        vacancies.addAll(newVacancyList)
        notifyDataSetChanged()
    }

    interface ListItemClickListener {
        fun onListItemClick(vacancy: VacancySimilarShortUiModel)
    }

}

