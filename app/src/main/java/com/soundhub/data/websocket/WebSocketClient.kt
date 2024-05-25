package com.soundhub.data.websocket

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.requests.SendMessageRequest
import com.soundhub.utils.ApiEndpoints.ChatWebSocket.WS_DELETE_MESSAGE_ENDPOINT
import com.soundhub.utils.ApiEndpoints.ChatWebSocket.WS_READ_MESSAGE_ENDPOINT
import com.soundhub.utils.ApiEndpoints.ChatWebSocket.WS_SEND_MESSAGE_ENDPOINT
import com.soundhub.utils.HttpUtils.Companion.AUTHORIZATION_HEADER
import com.soundhub.utils.HttpUtils.Companion.getBearerToken
import com.soundhub.utils.constants.Constants.DYNAMIC_PARAM_REGEX
import io.reactivex.disposables.Disposable
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompCommand
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import java.util.UUID

class WebSocketClient(private val accessToken: String?) {
    private var stompClient: StompClient? = null
    private val gson = Gson()

    fun connect(url: String) {
        val bearerToken: String = getBearerToken(accessToken)
        val header: Map<String, String> = mapOf(AUTHORIZATION_HEADER to bearerToken)
        val stompHeader = StompHeader(AUTHORIZATION_HEADER, bearerToken)

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url, header)
        stompClient?.connect(listOf(stompHeader))
        Log.i("WebSocketClient", "Connected successfully")
    }

    fun sendMessage(
        messageRequest: SendMessageRequest?,
        onComplete: () -> Unit = {},
        onError: (e: Throwable) -> Unit = {}
    ): Disposable? {
        val jsonObject: String = gson.toJson(messageRequest)
        return stompClient
            ?.send(WS_SEND_MESSAGE_ENDPOINT, jsonObject)
            ?.subscribe({ onComplete() }, { onError(it) })
    }

    fun readMessage(
        messageId: UUID,
    ): Disposable? {
        val endpoint: String = replaceParam(messageId, WS_READ_MESSAGE_ENDPOINT)
        return stompClient
            ?.send(endpoint, null)
            ?.subscribe()
    }

    fun deleteMessage(
        messageId: UUID,
    ): Disposable? {
        val endpoint: String = replaceParam(messageId, WS_DELETE_MESSAGE_ENDPOINT)
        val bearerToken: String = getBearerToken(accessToken)

        val stompMessage = StompMessage(
            StompCommand.SEND,
            listOf(
                StompHeader(AUTHORIZATION_HEADER, bearerToken),
                StompHeader("destination", endpoint)),
            null
        )
        return stompClient
            ?.send(stompMessage)
            ?.subscribe(
                { Log.d("WebSocketClient", "deleteMessage[1]: message with id $messageId deleted successfully") },
                { Log.e("WebSocketClient", "deleteMessage[2]: ${it.stackTraceToString()}") }
            )
    }

    fun subscribe(
        topic: String,
        messageListener: (StompMessage) -> Unit,
        errorListener: (Throwable) -> Unit = {}
    ): Disposable? {
        return stompClient?.topic(topic)?.subscribe(
            { message -> messageListener(message) },
            { error ->
                Log.e("WebSocketClient", "subscribe[error]: ${error.stackTraceToString()}")
                errorListener(error)
            }
        )
    }

    fun disconnect(){
        stompClient?.disconnect()
        Log.i("WebSocketClient", "Disconnected")
    }

    private fun replaceParam(id: UUID, endpoint: String): String {
        val regex = Regex(DYNAMIC_PARAM_REGEX)
        return regex.replace(endpoint, id.toString())
    }
}
