package com.soundhub.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.soundhub.utils.extensions.intent.getSerializableExtraExtended
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class MessageReceiver<T: Serializable>(
	private var messageHandler: suspend (T) -> Unit = {},
	private val intentName: String,
	private val clazz: Class<T>
): BroadcastReceiver() {
	val coroutineScope = CoroutineScope(Dispatchers.IO)

	override fun onReceive(context: Context?, intent: Intent?) {
		val message = intent?.getSerializableExtraExtended(intentName, clazz)
		coroutineScope.launch {
			message?.let {
				Log.d("MessengerReceiver", "broadcast receiver: $it")
				messageHandler(it)
			}
		}
	}
}