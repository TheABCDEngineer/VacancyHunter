package ru.practicum.android.diploma.root.data

import com.google.gson.Gson
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Address
import ru.practicum.android.diploma.features.vacancydetails.domain.models.ContactPhone
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.data.db.entity.FavVacancyEntity

class VacancyDbConverter(private val gson: Gson) {

    fun mapToVacancyEntity(vacancy: VacancyDetails): FavVacancyEntity {
        return FavVacancyEntity(
            vacancyId = vacancy.vacancyId,
            vacancyName = vacancy.vacancyName,
            salary = gson.toJson(vacancy.salary),
            logoUrl = vacancy.logoUrl,
            employerName = vacancy.employerName,
            employerArea = vacancy.employerArea,
            experienceReq = vacancy.experienceReq,
            employmentType = vacancy.employmentType,
            scheduleType = vacancy.scheduleType,
            vacancyDescription = vacancy.vacancyDescription,
            keySkills = gson.toJson(vacancy.keySkills),
            contactsName = vacancy.contactsName,
            contactsEmail = vacancy.contactsEmail,
            contactsPhones = gson.toJson(vacancy.contactsPhones),
            shareVacancyUrl = vacancy.shareVacancyUrl,
            employerAddress = gson.toJson(vacancy.employerAddress),
            createdTimeStamp = System.currentTimeMillis()
        )
    }

    fun mapToVacancyDetails(vacancy: FavVacancyEntity): VacancyDetails {
        return VacancyDetails(
            vacancyId = vacancy.vacancyId,
            vacancyName = vacancy.vacancyName,
            salary = convertFromJsonToSalary(vacancy.salary),
            logoUrl = vacancy.logoUrl,
            employerName = vacancy.employerName,
            employerArea = vacancy.employerArea,
            experienceReq = vacancy.experienceReq,
            employmentType = vacancy.employmentType,
            scheduleType = vacancy.scheduleType,
            vacancyDescription = vacancy.vacancyDescription,
            keySkills = convertFromJsonToKeySkills(vacancy.keySkills),
            contactsName = vacancy.contactsName,
            contactsEmail = vacancy.contactsEmail,
            contactsPhones = convertFromJsonToPhones(vacancy.contactsPhones),
            shareVacancyUrl = vacancy.shareVacancyUrl,
            employerAddress = convertFromJsonToAddress(vacancy.employerAddress),
            isFavorite = true
        )
    }

    private fun convertFromJsonToSalary(salary: String): Salary? {
        return when (salary) {
            "" -> null
            else -> { gson.fromJson(salary, Salary::class.java) }
        }
    }

    private fun convertFromJsonToKeySkills(keySkills: String): List<String> {
        return when (keySkills) {
            "" -> emptyList()
            else -> { gson.fromJson(keySkills, Array<String>::class.java).toList() }
        }
    }

    private fun convertFromJsonToPhones(phones: String): List<ContactPhone> {
        return when (phones) {
            "" -> emptyList()
            else -> { gson.fromJson(phones, Array<ContactPhone>::class.java).toList() }
        }
    }

    private fun convertFromJsonToAddress(address: String): Address? {
        return when (address) {
            "" -> null
            else -> { gson.fromJson(address, Address::class.java) }
        }
    }
}
