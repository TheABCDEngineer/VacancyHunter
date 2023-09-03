package ru.practicum.android.diploma.features.search.data.network.dto

import com.google.gson.annotations.SerializedName

data class VacancyShortDto(
    @SerializedName("id") val id: String,
    @SerializedName("salary") val salary: Salary,
    @SerializedName("name") val jobTitle: String,
    @SerializedName("area") val region: VacancyArea,
    @SerializedName("employer") val employer: Employer
) {
    data class Salary(
        @SerializedName("currency") val currency: String?,
        @SerializedName("from") val from: Int?,
        @SerializedName("to") val to: Int?
    )
    data class VacancyArea(
        @SerializedName("name") val area: String
    )
    data class Employer(
        @SerializedName("logo_urls") val logoUrls: LogoUrls?,
        @SerializedName("name") val employerName: String
    ) {
        data class LogoUrls(
            @SerializedName("90") val logoUrl90: String
        )
    }
}
