package com.soundhub.utils.mappers

import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.model.Artist
import com.soundhub.data.states.RegistrationState
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper
interface RegisterDataMapper {
	@Mapping(
		target = "favoriteArtistsIds",
		source = "favoriteArtists",
		qualifiedByName = ["mapArtistsToIds"]
	)
	@Mapping(target = "online", ignore = true)
	@Mapping(target = "lastOnline", ignore = true)
	fun registerStateToRegisterRequestBody(
		registrationState: RegistrationState?
	): RegisterRequestBody

	companion object {
		val impl: RegisterDataMapper = Mappers.getMapper(RegisterDataMapper::class.java)

		@JvmStatic
		@Named("mapArtistsToIds")
		fun mapArtistsToIds(list: List<Artist>): List<Int> = list.map { it.id }
	}
}