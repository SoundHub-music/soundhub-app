package com.soundhub.utils

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.Message
import com.soundhub.utils.enums.MediaFolder
import com.soundhub.utils.lib.HttpUtils
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class NotificationHelper(
	private val context: Context,
	userCredsStore: UserCredsStore
) {
	companion object {
		val LOG_CLASSNAME = this::class.simpleName
	}

	private var notificationManager: NotificationManagerCompat =
		NotificationManagerCompat.from(context)

	private val userCredsFlow = userCredsStore.getCreds()

	fun generateNotificationId(message: Message) = message.author?.id.hashCode()

	fun cancelNotification(notificationId: Int) {
		Log.i(LOG_CLASSNAME, "Notification with $notificationId canceled")
		notificationManager.cancel(notificationId)
	}

	fun createNotificationChannelIfNotExists(
		channelId: String,
		channelName: String,
		importance: Int,
		setExtraParameters: (NotificationChannel) -> Unit = {}
	) {
		val channel: NotificationChannel? = notificationManager
			.notificationChannels.firstOrNull { c ->
				c.id == channelId
			}

		if (channel == null) {
			Log.i(LOG_CLASSNAME, "Channel with id $channelId was not found. Creating...")
			val newChannel = NotificationChannel(channelId, channelName, importance).apply {
				setExtraParameters(this)
			}

			notificationManager.createNotificationChannel(newChannel)
		}
	}

	fun createNotificationGroupIfNotExists(groupId: String) {
		val group = notificationManager.notificationChannelGroups.firstOrNull { g ->
			g.id == groupId
		}

		if (group == null) {
			val newGroup = NotificationChannelGroup(groupId, "group_name")
			notificationManager.createNotificationChannelGroup(newGroup)
		}
	}

	fun loadAvatar(
		message: Message,
		notificationBuilder: Builder
	) = runBlocking {
		val userCreds = userCredsFlow.firstOrNull()

		Glide.with(context)
			.asBitmap()
			.load(
				HttpUtils.prepareGlideUrWithAccessToken(
					userCreds,
					message.author?.avatarUrl,
					MediaFolder.AVATAR
				)
			)
			.circleCrop()
			.into(object : CustomTarget<Bitmap>() {
				override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
					val person = Person.Builder()
						.setName(message.author?.getFullName())
						.setKey(message.author?.getFullName())
						.setIcon(IconCompat.createWithBitmap(resource))
						.build()

					val messagingStyle = NotificationCompat.MessagingStyle(person)
						.addMessage(
							message.content,
							System.currentTimeMillis(),
							person
						)

					notificationBuilder.setStyle(messagingStyle)
				}

				override fun onLoadCleared(placeholder: Drawable?) {}
			})
	}

	fun createPendingIntent(
		action: String,
		requestCode: Int,
		flags: Int,
		extras: Bundle = Bundle()
	): PendingIntent {
		val intent = Intent(context, context::class.java).apply {
			this.action = action
			putExtras(extras)
		}

		return PendingIntent.getService(
			context,
			requestCode,
			intent,
			flags
		)
	}

	fun sendNotification(notificationBuilder: Builder, id: Int) {
		if (ActivityCompat.checkSelfPermission(
				context,
				android.Manifest.permission.POST_NOTIFICATIONS
			) == PackageManager.PERMISSION_GRANTED
		) {
			notificationManager.notify(id, notificationBuilder.build())
		}
	}
}
