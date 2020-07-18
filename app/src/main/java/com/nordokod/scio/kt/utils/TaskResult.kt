package com.nordokod.scio.kt.utils

sealed class TaskResult<out T> {
    data class Success<out T>(val data: T) : TaskResult<T>()
    data class Error(val e: Exception) : TaskResult<Nothing>()
}