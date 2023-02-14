package com.actiangent.note.data

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    companion object {
        @Suppress("unused")
        inline fun <T> Result<T>.getOrElse(fallback: (Throwable) -> T) = when (this) {
            is Success -> data
            is Error -> fallback(exception)
        }
    }
}