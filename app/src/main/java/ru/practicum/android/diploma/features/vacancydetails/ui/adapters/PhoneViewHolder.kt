package ru.practicum.android.diploma.features.vacancydetails.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ListItemPhoneBinding
import ru.practicum.android.diploma.features.vacancydetails.domain.models.ContactPhone

class PhoneViewHolder (
    private val binding: ListItemPhoneBinding,
    private val clickListener: PhonesAdapter.ListItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(phone: ContactPhone) {

        binding.apply {
            binding.phone.text = phone.phoneNumber
            binding.phoneComment.text = phone.phoneComment
        }

        binding.phone.setOnClickListener {
            clickListener.onPhoneClick(phoneNumber = phone.phoneNumber)
        }
    }
}