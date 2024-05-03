package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import com.soundhub.data.enums.InviteStatus
import java.time.LocalDateTime
import java.util.UUID

data class Invite(
//    val id: UUID,
    @SerializedName("createdDateTime")
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    @SerializedName("sender")
    val sender: User,

    @SerializedName("recipient")
    val recipient: User,

    @SerializedName("status")
    val status: InviteStatus = InviteStatus.CONSIDERED
): Notification(
    type = NotificationType.FRIEND_REQUEST,
    dateTime = createdDateTime
) { init { obj = this } }


open class Notification(
    val id: UUID = UUID.randomUUID(),
    val type: NotificationType,
    var obj: Any? = null,
    val dateTime: LocalDateTime = LocalDateTime.now(),
) {

}

enum class NotificationType {
    FRIEND_REQUEST,
    NEW_POST,
    NEW_EVENT
}