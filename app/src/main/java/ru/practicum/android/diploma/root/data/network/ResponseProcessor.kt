package ru.practicum.android.diploma.root.data.network

import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.data.network.models.ResponseData
import ru.practicum.android.diploma.root.domain.model.Outcome

class ResponseProcessor {

    fun <R, O> processResponse(
        response: ResponseData<R>,
        mapper: (R) -> O
    ): Outcome<O> {

        return when (response.resultCode) {

            NetworkResultCode.SUCCESS -> {
                if (response.data != null) {
                    Outcome.Success(data = mapper(response.data!!))
                } else {
                    Outcome.Error(status = NetworkResultCode.SERVER_ERROR, data = null)
                }
            }

            NetworkResultCode.CONNECTION_ERROR -> {
                Outcome.Error(status = NetworkResultCode.CONNECTION_ERROR, data = null)
            }

            else -> {
                Outcome.Error(status = NetworkResultCode.UNKNOWN_ERROR, data = null)
            }
        }
    }

}