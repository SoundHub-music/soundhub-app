package com.soundhub.utils.mappers

import com.soundhub.data.api.requests.SendMessageRequest
import com.soundhub.data.model.Chat
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.util.UUID

@Mapper
interface MessageMapper {
	@Mapping(target = "chat.id", ignore = true)
	@Mapping(target = "chat.messages", ignore = true)
	@Mapping(target = "chat.participants", ignore = true)
	@Mapping(target = "chat.isGroup", ignore = true)
	@Mapping(target = "chat.chatName", ignore = true)
	@Mapping(target = "chat.createdBy", ignore = true)
	@Mapping(target = "userId", source = "userId")
	@Mapping(target = "chatId", source = "chat", qualifiedByName = ["mapChatToId"])
	@Mapping(target = "content", source = "content")
	fun toSendMessageRequest(
		chat: Chat?,
		userId: UUID?,
		content: String,
		replyToMessageId: UUID? = null
	): SendMessageRequest?

	companion object {
		val impl: MessageMapper = Mappers.getMapper(MessageMapper::class.java)

		@JvmStatic
		@Named("mapChatToId")
		fun mapChatToId(chat: Chat?): UUID? = chat?.id
	}
}