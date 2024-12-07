package com.soundhub.domain.model

import com.google.gson.annotations.SerializedName
import com.soundhub.data.enums.InviteStatus
import com.soundhub.data.enums.NotificationType
import java.time.LocalDateTime
import java.util.UUID

data class Invite(
	@SerializedName("createdDateTime")
	val createdDateTime: LocalDateTime = LocalDateTime.now(),

	@SerializedName("sender")
	val sender: User,

	@SerializedName("recipient")
	val recipient: User,

	@SerializedName("status")
	val status: InviteStatus = InviteStatus.CONSIDERED
) : Notification(
	type = NotificationType.FRIEND_REQUEST,
	dateTime = createdDateTime
) {
	init {
		obj = this
	}
}


open class Notification(
	val id: UUID = UUID.randomUUID(),
	val type: NotificationType,
	var obj: Any? = null,
	val dateTime: LocalDateTime = LocalDateTime.now(),
)