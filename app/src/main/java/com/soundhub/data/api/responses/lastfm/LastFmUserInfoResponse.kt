package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName
import com.soundhub.domain.model.LastFmUser

data class LastFmUserInfoResponse(
	@SerializedName("user")
	val user: LastFmUser
)

