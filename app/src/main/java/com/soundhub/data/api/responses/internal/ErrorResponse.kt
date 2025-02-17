package com.soundhub.data.api.responses.internal

data class ErrorResponse(
	override var message: String? = null,
	val error: Int? = null,
	val type: String? = null,
	val title: String? = null,
	var status: Int? = null,
	var detail: String? = null,
	val instance: String? = null
) : Exception()