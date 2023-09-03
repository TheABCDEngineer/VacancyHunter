package ru.practicum.android.diploma.root.domain.model

import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode

sealed class Outcome<T>(val data: T? = null, val status: NetworkResultCode? = null) {
    class Success<T>(data: T, status: NetworkResultCode = NetworkResultCode.SUCCESS): Outcome<T>(data, status)
    class Error<T>(data: T? = null, status: NetworkResultCode): Outcome<T>(data, status)
}
