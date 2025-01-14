package ru.practicum.android.diploma.features.vacancydetails.data.models

import ru.practicum.android.diploma.features.vacancydetails.domain.models.Address
import ru.practicum.android.diploma.features.vacancydetails.domain.models.ContactPhone
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Metro
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Salary
import ru.practicum.android.diploma.features.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.root.data.network.models.Response

class VacancyDetailsMapper : (Response) -> VacancyDetails {

    override fun invoke(dto: Response): VacancyDetails {
        dto as VacancyDetailsDto
        return VacancyDetails(
            vacancyId = dto.vacancyId,
            vacancyName = dto.vacancyName ?: "",
            salary = getSalary(dto.salary),
            logoUrl = dto.employer?.logoUrls?.logoOriginal ?: "",
            employerName = dto.employer?.employerName ?: "",
            employerArea = dto.vacancyArea?.area ?: "",
            experienceReq = dto.experience?.experience ?: "",
            employmentType = dto.employmentType?.employmentTypeName ?: "",
            scheduleType = dto.scheduleType?.scheduleTypeName ?: "",
            vacancyDescription = dto.vacancyDesc ?: "",
            keySkills = getSkillsList(dto.keySkills),
            contactsName = dto.contacts?.contactsName ?: "",
            contactsEmail = dto.contacts?.contactsEmail ?: "",
            contactsPhones = getPhones(dto.contacts?.phones),
            shareVacancyUrl = dto.alternateUrl ?: "",
            employerAddress = getAddress(dto.address),
            isFavorite = false
        )
    }

    private fun getSkillsList(keySkills: List<VacancyDetailsDto.KeySkill>?): List<String> {
        return keySkills?.map {
            it.skillName
        } ?: emptyList()
    }

    private fun getPhones(phones: List<VacancyDetailsDto.PhoneDto>?): List<ContactPhone> {
        return phones?.map {
            ContactPhone(
                phoneNumber = it.phoneNumber ?: "",
                phoneComment = it.comment ?: ""
            )
        } ?: emptyList()
    }

    private fun getSalary(salaryDto: VacancyDetailsDto.SalaryDto?): Salary? {
        return when (salaryDto) {
            null -> null
            else -> Salary(
                salaryCurrency = salaryDto.currency,
                salaryLowerBoundary = salaryDto.from,
                salaryUpperBoundary = salaryDto.to
            )
        }
    }

    private fun getAddress(addressDto: VacancyDetailsDto.EmployerAddressDto?): Address? {
        return when (addressDto) {
            null -> null
            else -> Address(
                building = addressDto.building ?: "",
                city = addressDto.city ?: "",
                street = addressDto.street ?: "",
                addressNote = addressDto.addressNote ?: "",
                metroStations = getMetroList(addressDto.metroStations)
            )
        }
    }

    private fun getMetroList(listMetroDto: List<VacancyDetailsDto.MetroDto>?): List<Metro> {
        return when (listMetroDto) {
            null -> emptyList()
            else -> listMetroDto.map {
                Metro(
                    lineName = it.lineName ?: "",
                    stationName = it.stationName ?: ""
                )
            }
        }
    }

}