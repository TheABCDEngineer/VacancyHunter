package ru.practicum.android.diploma.features.vacancydetails.data.models

import com.google.gson.annotations.SerializedName

data class VacancyDetailsDto(

    @SerializedName("id") val vacancyId: String,
    @SerializedName("name") val vacancyName: String?,
    @SerializedName("salary") val salary: SalaryDto?,
    @SerializedName("employer") val employer: Employer?,
    @SerializedName("area") val vacancyArea: VacancyArea?,
    @SerializedName("experience") val experience: Experience?,
    @SerializedName("employment") val employmentType: EmploymentType?,
    @SerializedName("schedule") val scheduleType: ScheduleType?,
    @SerializedName("description") val vacancyDesc: String?,
    @SerializedName("key_skills") val keySkills: List<KeySkill>?,
    @SerializedName("contacts") val contacts: Contacts?,
    @SerializedName("alternate_url") val alternateUrl: String?,
    @SerializedName("professional_roles") val profRoles: List<ProfRole>?

) {

    data class KeySkill(
        @SerializedName("name") val skillName: String
    )

    data class Contacts(
        @SerializedName("email") val contactsEmail: String?,
        @SerializedName("name") val contactsName: String?,
        @SerializedName("phones") val phones: List<PhoneDto>?
    )

    data class SalaryDto(
        @SerializedName("currency") val currency: String?,
        @SerializedName("from") val from: Int?,
        @SerializedName("to") val to: Int?
    )

    data class Employer(
        @SerializedName("logo_urls") val logoUrls: LogoUrls?,
        @SerializedName("name") val employerName: String

    ) {
        data class LogoUrls(
            @SerializedName("original") val logoOriginal: String
        )
    }

    data class EmploymentType(
        @SerializedName("name") val employmentTypeName: String
    )

    data class Experience(
        @SerializedName("name") val experience: String
    )

    data class PhoneDto(
        @SerializedName("formatted") val phoneNumber: String?,
        @SerializedName("comment") val comment: String?
    )

    data class ScheduleType(
        @SerializedName("name") val scheduleTypeName: String
    )

    data class VacancyArea(
        @SerializedName("name") val area: String
    )

    data class  ProfRole(
        @SerializedName("id") val profRoleId: String
    )

}
