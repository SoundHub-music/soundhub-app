package com.soundhub.utils.lib

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    class StringResource(
        @StringRes
        var srcId: Int,
        vararg var args: Any?
    ): UiText()

    fun getString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(srcId, *args)
        }
    }
}
