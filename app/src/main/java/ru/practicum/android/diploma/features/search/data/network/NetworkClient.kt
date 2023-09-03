package ru.practicum.android.diploma.features.search.data.network

import ru.practicum.android.diploma.root.data.network.models.Response

interface NetworkClient {
    suspend fun executeRequest(request: Any): Response<Any>
}