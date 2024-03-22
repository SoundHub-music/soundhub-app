package com.soundhub.utils

import android.content.Context
import androidx.annotation.StringRes
sealed class UiText {
    data class DynamicString(val value: String): UiText()
    class StringResource(
        @StringRes
        val srcId: Int,
        vararg val args: Any?
    ): UiText()

    fun getString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(srcId, *args)
        }
    }
}
