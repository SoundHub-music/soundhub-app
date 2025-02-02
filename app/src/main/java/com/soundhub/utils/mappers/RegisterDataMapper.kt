package com.soundhub.utils.mappers

import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.domain.model.Artist
import com.soundhub.domain.states.RegistrationState
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.util.UUID

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
		fun mapArtistsToIds(list: List<Artist>): List<UUID> = list.map { it.id }
	}
}