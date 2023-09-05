package ru.practicum.android.diploma.root.presentation.ui.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.root.presentation.model.VacancyScreenModel

class VacancyAdapter(
    private val vacancyList: ArrayList<VacancyScreenModel>,
    private val onItemClickedAction: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.vacancy_rv_placeholder
            vacancyList.size + 1 -> R.layout.vacancy_rv_loading
            else -> R.layout.vacancy_rv_card
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.vacancy_rv_placeholder,
            R.layout.vacancy_rv_loading -> PlaceholderViewHolder(view)
            else -> VacancyViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VacancyViewHolder -> {
                holder.bind(vacancyList[position-1])
                holder.itemView.setOnClickListener {
                    onItemClickedAction.invoke(vacancyList[position-1].id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return vacancyList.size + 2
    }

    fun updateItems(items: ArrayList<VacancyScreenModel>) {
        vacancyList.clear()
        addItems(items)
    }

    fun addItems(items: ArrayList<VacancyScreenModel>) {
        vacancyList.addAll(items)
        this.notifyDataSetChanged()
    }
}