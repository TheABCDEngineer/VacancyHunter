package ru.practicum.android.diploma.features.vacancydetails.domain.models

data class Address(
    val building: String,
    val city: String,
    val street: String,
    val addressNote: String,
    val metroStations: List<Metro>
) {
    fun isEmpty(): Boolean {
        return (building.isEmpty() && city.isEmpty() && street.isEmpty() && addressNote.isEmpty() && metroStations.isEmpty())
    }
}