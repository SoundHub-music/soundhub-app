package com.soundhub.data.api.responses.discogs

interface IDiscogsResponse<T> {
	val results: List<T>
	val pagination: DiscogsResponsePagination
}

data class DiscogsResponse(
	override val results: List<DiscogsEntityResponse>,
	override val pagination: DiscogsResponsePagination
) : IDiscogsResponse<DiscogsEntityResponse>