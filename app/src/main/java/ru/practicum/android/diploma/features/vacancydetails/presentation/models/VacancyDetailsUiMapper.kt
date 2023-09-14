package ru.practicum.android.diploma.features.vacancydetails.presentation.models

import android.content.Context
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Address
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.presentation.ui.SalaryFormat

class VacancyDetailsUiMapper(private val context: Context) :
        (VacancyDetails) -> VacancyDetailsUiModel {

    override fun invoke(model: VacancyDetails): VacancyDetailsUiModel {
        return VacancyDetailsUiModel(
            vacancyId = model.vacancyId,
            vacancyName = model.vacancyName,
            salary = formatSalaryString(model.salary),
            logoUrl = model.logoUrl,
            employerName = model.employerName,
            employerAddress = getFormattedAddress(model.employerArea, model.employerAddress),
            experience = model.experienceReq,
            employmentTypes = formatEmploymentTypes(model.employmentType, model.scheduleType),
            vacancyDescription = model.vacancyDescription,
            keySkills = formatKeySkills(model.keySkills),
            contactsName = model.contactsName,
            contactsEmail = model.contactsEmail,
            contactsPhones = model.contactsPhones,
            shareVacancyUrl = model.shareVacancyUrl,
            isFavorite = model.isFavorite
        )
    }

    private fun formatKeySkills(keySkills: List<String>): String {
        return keySkills
            .filter { it.isNotEmpty() }
            .joinToString("\n")
    }

    private fun formatEmploymentTypes(employmentType: String, scheduleType: String): String {
        return listOf(employmentType, scheduleType)
            .filter { it.isNotEmpty() }
            .joinToString(", ")
    }

    private fun formatSalaryString(salaryObj: Salary?): String {
        return SalaryFormat.formatSalaryString(salaryObj, context)
    }

    private fun getFormattedAddress(area: String, address: Address?): String {
        if (address == null || address.isEmpty()) return area
        return formatAddress(address)
    }

    private fun formatAddress(addressObj: Address): String {
        val address = arrayListOf<String>()
        if (addressObj.city.isNotEmpty()) address.add(addressObj.city)
        if (addressObj.metroStations.isNotEmpty()) addressObj.metroStations.map {
            if (it.lineName.isNotEmpty()) address.add(it.lineName)
            if (it.stationName.isNotEmpty()) address.add(it.stationName)
        }
        if (addressObj.street.isNotEmpty()) address.add(addressObj.street)
        if (addressObj.building.isNotEmpty()) address.add(addressObj.building)
        if (addressObj.addressNote.isNotEmpty()) address.add(addressObj.addressNote)
        return address.joinToString(", ")
    }
}