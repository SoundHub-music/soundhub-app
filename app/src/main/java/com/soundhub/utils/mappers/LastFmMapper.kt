package com.soundhub.utils.mappers

import com.soundhub.data.api.responses.lastfm.LastFmSessionBody
import com.soundhub.data.model.LastFmUser
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface LastFmMapper {
	@Mapping(target = "sessionKey", source = "key")
	fun lastFmSessionBodyToLastFmUser(session: LastFmSessionBody?): LastFmUser?

	companion object {
		val impl: LastFmMapper = Mappers.getMapper(LastFmMapper::class.java)
	}
}