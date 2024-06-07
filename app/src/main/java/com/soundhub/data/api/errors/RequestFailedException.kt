package com.soundhub.data.api.errors

class RequestFailedException(
    override val message: String?,
    val throwable: Throwable?
): Exception(message, throwable)