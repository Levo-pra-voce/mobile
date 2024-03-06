package com.levopravoce.mobile.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.levopravoce.mobile.BuildConfig
import com.levopravoce.mobile.features.app.data.dto.MessageSocketDTO
import com.levopravoce.mobile.features.auth.domain.AuthStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import javax.inject.Inject
import javax.inject.Singleton

private data class Event(
    val command: String,
    val headers: Map<String, String>,
    val body: String? = null
)


class WebSocketEventDTO(headers: Map<String, String>, body: String?) {
    val headers: Map<String, String>? = null
    val body: String? = null

    enum class HEADERS(
        val value: String
    ) {
        DESTINATION("destination")
    }
}

enum class Destination(val value: String) {
    CHAT("/chat"),
}

@Singleton
class WebSocketClient @Inject constructor(
    private val authStore: AuthStore
) {
    private var webSocket: WebSocket? = null
    private var isConnected: Boolean = false
    private val gson = GsonBuilder().serializeNulls().create();
    var callback: ((MessageSocketDTO) -> Unit)? = null

    val messagesFlow = MutableStateFlow<MessageSocketDTO?>(null);

    private val webSocketListener: okhttp3.WebSocketListener = object : okhttp3.WebSocketListener(
    ) {

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            try {
                runBlocking(Dispatchers.IO) {
                    val message = gson.fromJson(text, MessageSocketDTO::class.java)
                    messagesFlow.emit(message)
                    callback?.invoke(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            isConnected = false
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            isConnected = true
        }
    }

    suspend fun connect() {
        if (!isConnected) {
            val token: String? = authStore.getAccessToken.first {
                it?.isNotEmpty() ?: false
            }

            val request = Request.Builder()
                .header("Authorization", "Bearer $token")
                .url(BuildConfig.SOCKET_URL).build()
            val client = OkHttpClient.Builder().build()
            val websocket = client.newWebSocket(request, webSocketListener)

            this.webSocket = websocket
        }
    }

    fun disconnect() {
        webSocket?.close(1000, null)
    }

    fun isConnected(): Boolean {
        return isConnected
    }

    fun <T> send(destination: Destination, body: T? = null) {
        send<Any>(destination, mutableMapOf(), body)
    }

    fun <T> send(
        destination: Destination,
        headers: MutableMap<String, String> = mutableMapOf(),
        body: T? = null
    ) {
        headers[WebSocketEventDTO.HEADERS.DESTINATION.value] = destination.value

        val webSocketEventDTO = WebSocketEventDTO(
            headers = headers,
            body = gson.toJson(body)
        )

        webSocket?.send(gson.toJson(webSocketEventDTO))
    }
}
