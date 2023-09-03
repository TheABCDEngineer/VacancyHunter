package ru.practicum.android.diploma.root.data.network

import ru.practicum.android.diploma.root.data.network.models.Response

interface NetworkSearch {
    suspend fun executeRequest(request: Any): Response<Any>
}