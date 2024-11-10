package com.soundhub.utils.extensions.context_wrapper

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.IntentFilter
import android.os.Build

@SuppressLint("UnspecifiedRegisterReceiverFlag")
fun ContextWrapper.registerReceiverExtended(
	receiver: BroadcastReceiver,
	intentFilter: IntentFilter,
) = when {
	Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
		registerReceiver(
			receiver,
			intentFilter,
			Context.RECEIVER_NOT_EXPORTED
		)

	else -> registerReceiver(
		receiver,
		intentFilter
	)
}