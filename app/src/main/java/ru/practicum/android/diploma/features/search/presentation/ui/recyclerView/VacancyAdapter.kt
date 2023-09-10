package ru.practicum.android.diploma.features.search.presentation.ui.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.root.presentation.model.VacancyScreenModel

class VacancyAdapter(
    private val vacancyList: ArrayList<VacancyScreenModel>,
    private val onItemClickedAction: (String) -> Unit,
    private val onContinueLoading: () -> Unit = {},
    private var isContinueLoading: Boolean = true
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> PLACEHOLDER
            vacancyList.size + 1 -> LOADING
            else -> VACANCY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return when (viewType) {
            PLACEHOLDER, LOADING -> PlaceholderViewHolder(view)
            else -> VacancyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) return

        if (holder is VacancyViewHolder) {
            holder.bind(vacancyList[position - 1])
            holder.itemView.setOnClickListener {
                onItemClickedAction.invoke(vacancyList[position - 1].id)
            }
        }
        if (position == itemCount-3 && isContinueLoading) onContinueLoading.invoke()
    }

    override fun getItemCount(): Int {
        if (isContinueLoading) return vacancyList.size + 2
        return vacancyList.size + 1
    }

    fun updateItems(items: ArrayList<VacancyScreenModel>, isContinueLoading: Boolean) {
        this.isContinueLoading = isContinueLoading
        vacancyList.clear()
        vacancyList.addAll(items)
        notifyDataSetChanged()
    }

    fun addItems(items: ArrayList<VacancyScreenModel>, isContinueLoading: Boolean) {
        val insertedItemCount = if (isContinueLoading) items.size else items.size-1
        val lastPosition = itemCount - 1
        this.isContinueLoading = isContinueLoading
        if (items.isEmpty()) {
            notifyItemRemoved(lastPosition)
            return
        }
        vacancyList.addAll(items)
        notifyItemRangeInserted(lastPosition,insertedItemCount)
    }

    companion object {
        private val PLACEHOLDER = R.layout.vacancy_rv_placeholder
        private val VACANCY = R.layout.vacancy_rv_card
        private val LOADING = R.layout.vacancy_rv_loading
    }
}