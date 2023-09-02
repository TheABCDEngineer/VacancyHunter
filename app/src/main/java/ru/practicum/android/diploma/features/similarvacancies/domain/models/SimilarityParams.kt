package ru.practicum.android.diploma.features.similarvacancies.domain.models

data class SimilarityParams(
    val vacancyId: String,
    val profRoles: List<String>
)