package ru.practicum.android.diploma.root.data

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.root.ApplicationConstants
import ru.practicum.android.diploma.root.domain.repository.FilterStorage

class FilterStorageImplSharedPref(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
): FilterStorage {
    private val key = ApplicationConstants.FILTERS_STORAGE_KEY

    override fun saveFilter(model: Filter?) {
        val json =
            if (model != null) gson.toJson(model) else null
        sharedPreferences.edit()
            .putString(key, json)
            .apply()
    }

    override fun getFilter(): Filter? {
        val json: String = sharedPreferences.getString(key, null) ?: return null
        return gson.fromJson(json, Filter::class.java)
    }
}