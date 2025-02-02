package com.soundhub.data.api.services

import com.soundhub.data.api.responses.lastfm.ArtistsByTagResponse
import com.soundhub.data.api.responses.lastfm.LastFmArtistInfo
import com.soundhub.data.api.responses.lastfm.LastFmSessionResponse
import com.soundhub.data.api.responses.lastfm.LastFmUserInfoResponse
import com.soundhub.data.api.responses.lastfm.SearchArtistResponseBody
import com.soundhub.utils.constants.ApiEndpoints.LastFm.API_SIG_PARAM
import com.soundhub.utils.constants.ApiEndpoints.LastFm.ARTIST_PARAM
import com.soundhub.utils.constants.ApiEndpoints.LastFm.GET_ARTIST_INFO
import com.soundhub.utils.constants.ApiEndpoints.LastFm.GET_MOBILE_SESSION
import com.soundhub.utils.constants.ApiEndpoints.LastFm.GET_TOP_ARTISTS_BY_TAG_ENDPOINT
import com.soundhub.utils.constants.ApiEndpoints.LastFm.GET_USER_INFO
import com.soundhub.utils.constants.ApiEndpoints.LastFm.LANG_PARAM
import com.soundhub.utils.constants.ApiEndpoints.LastFm.LIMIT_PARAM
import com.soundhub.utils.constants.ApiEndpoints.LastFm.MBID_PARAM
import com.soundhub.utils.constants.ApiEndpoints.LastFm.PAGE_PARAM
import com.soundhub.utils.constants.ApiEndpoints.LastFm.PASSWORD_PARAM
import com.soundhub.utils.constants.ApiEndpoints.LastFm.SEARCH_ARTISTS_ENDPOINT
import com.soundhub.utils.constants.ApiEndpoints.LastFm.TAG_PARAM
import com.soundhub.utils.constants.ApiEndpoints.LastFm.USERNAME_PARAM
import com.soundhub.utils.constants.ApiEndpoints.LastFm.USER_PARAM
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LastFmService {
	@GET(GET_TOP_ARTISTS_BY_TAG_ENDPOINT)
	suspend fun getArtistsByGenre(
		@Query(TAG_PARAM) tag: String,
		@Query(PAGE_PARAM) page: Int = 1,
		@Query(LIMIT_PARAM) limit: Int? = null
	): Response<ArtistsByTagResponse>

	@GET(GET_ARTIST_INFO)
	suspend fun getArtistInfo(
		@Query(ARTIST_PARAM) artist: String? = null,
		@Query(MBID_PARAM) mbid: String? = null,
		@Query(LANG_PARAM) lang: String? = null,
	): Response<LastFmArtistInfo>

	@GET(SEARCH_ARTISTS_ENDPOINT)
	suspend fun searchArtist(
		@Query(ARTIST_PARAM) artist: String?,
		@Query(PAGE_PARAM) page: Int = 1,
		@Query(LIMIT_PARAM) limit: Int? = null
	): Response<SearchArtistResponseBody>

	@POST(GET_MOBILE_SESSION)
	suspend fun getMobileSession(
		@Query(USERNAME_PARAM) username: String,
		@Query(PASSWORD_PARAM) password: String,
		@Query(API_SIG_PARAM) apiSig: String
	): Response<LastFmSessionResponse>

	@GET(GET_USER_INFO)
	suspend fun getUserInfo(
		@Query(USER_PARAM) username: String
	): Response<LastFmUserInfoResponse>
}