package com.example.arfashion.presentation.data

sealed class ARResult<out R> {
    data class Success<out T>(val data: T) : ARResult<T>()
    data class Error(val throwable: Throwable) : ARResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[throwable=$throwable]"
        }
    }
}

val ARResult<*>.succeeded
    get() = this is ARResult.Success && data != null

val <T> ARResult<T>.data: T?
    get() = (this as? ARResult.Success)?.data

fun <T, R> ARResult<T>.map(transform: (T) -> R): ARResult<R> {
    return when (this) {
        is ARResult.Success -> ARResult.Success(transform(data))
        is ARResult.Error -> ARResult.Error(throwable)
    }
}