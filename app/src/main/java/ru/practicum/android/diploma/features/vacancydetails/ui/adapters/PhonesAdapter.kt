package ru.practicum.android.diploma.features.vacancydetails.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ListItemPhoneBinding
import ru.practicum.android.diploma.features.vacancydetails.domain.models.ContactPhone

class PhonesAdapter (
    private val phones: List<ContactPhone>,
    private val clickListener: ListItemClickListener
) : RecyclerView.Adapter<PhoneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {

        val view = LayoutInflater.from(parent.context)

        return PhoneViewHolder(
            ListItemPhoneBinding.inflate(view, parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        holder.bind(phones[position])
    }

    override fun getItemCount(): Int {
        return phones.size
    }

    interface ListItemClickListener {
        fun onPhoneClick(phoneNumber: String)
    }

}

