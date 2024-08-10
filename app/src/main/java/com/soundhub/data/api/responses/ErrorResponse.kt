package com.soundhub.data.api.responses

data class ErrorResponse(
	val type: String? = null,
	val title: String? = null,
	var status: Int? = null,
	val detail: String? = null,
	val instance: String? = null
)