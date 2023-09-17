package ru.practicum.android.diploma.root.data.network

import ru.practicum.android.diploma.root.data.network.models.Request
import ru.practicum.android.diploma.root.data.network.models.Response
import ru.practicum.android.diploma.root.data.network.models.ResponseData

interface NetworkClient {
    suspend fun executeRequest(request: Request): ResponseData<Response>
}