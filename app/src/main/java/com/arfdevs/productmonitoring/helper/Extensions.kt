package com.arfdevs.productmonitoring.helper

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.gson.JsonSyntaxException
import javax.net.ssl.SSLPeerUnverifiedException

suspend fun <Input, Output> processResponse(
    result: NetworkResultWrapper<Input>,
    success: suspend (Input) -> DomainResult.Success<Output>
): DomainResult<Output> {
    return when (result) {
        is NetworkResultWrapper.Error -> mapErrors(result)
        is NetworkResultWrapper.Exception -> mapExceptions(result)
        is NetworkResultWrapper.Success -> success(result.data)
    }
}

private fun <Input, Output> mapErrors(
    result: NetworkResultWrapper.Error<Output>
): DomainResult<Input> {
    val emptyStateCodes = listOf(422, 404, 403, 409, 406, 401)

    return if (emptyStateCodes.contains(result.code)) DomainResult.EmptyState(
        "${result.message} : ${result.error}",
        result.code,
        result.code
    ) else DomainResult.ErrorState(
        "${result.message} : ${result.error}",
        result.code
    )
}

private fun <Input, Output> mapExceptions(
    result: NetworkResultWrapper.Exception<Output>
): DomainResult<Input> {
    return when (result.throwable) {
        is SSLPeerUnverifiedException -> DomainResult.TechnicalError(900)
        is JsonSyntaxException -> DomainResult.TechnicalError(901)
        else -> DomainResult.NetworkError
    }
}

fun Int?.orZero() = this ?: 0

fun View.enabled(state: Boolean) {
    this.isEnabled = state
}

fun View.visible(state: Boolean) {
    this.isVisible = state
}

fun View.goneIf(state: Boolean) {
    this.isGone = state
}
