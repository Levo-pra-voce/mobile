package com.levopravoce.mobile.config

import com.google.gson.Gson
import com.levopravoce.mobile.BuildConfig
import com.levopravoce.mobile.features.auth.domain.AuthStore
import kotlinx.coroutines.flow.first
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import javax.inject.Inject
import javax.inject.Singleton

object Command {
    const val SUBSCRIBE = "SUBSCRIBE"
    const val UNSUBSCRIBE = "UNSUBSCRIBE"
    const val SEND = "SEND"
}

private data class Event(
    val command: String,
    val headers: Map<String, String>,
    val body: String? = null
)

@Singleton
class WebSocketClient @Inject constructor(
    private val authStore: AuthStore
) {
    private var webSocket: WebSocket? = null
    private var isConnected: Boolean = false
    private var subscriptions: MutableMap<String, Pair<Int, (String, Headers) -> Unit>> =
        mutableMapOf()
    private var counter = 0
    private val gson = Gson()

    private val webSocketListener: okhttp3.WebSocketListener = object : okhttp3.WebSocketListener(
    ) {

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)

            val event = gson.fromJson(text, Event::class.java)

            val subscription = event.headers["subscription"]

            val callback = subscriptions[subscription]?.second

            if (callback != null) {
                callback(event.body ?: "", Headers.of(event.headers))
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

    fun subscribe(destination: String, callback: (String, Headers) -> Unit) {

        val header = mapOf(Pair("destination", destination), Pair("id", "sub-${counter++}"))
        val event = Event(Command.SUBSCRIBE, header)

        this.subscriptions[destination] = Pair(counter, callback)

        webSocket?.send(gson.toJson(event))
    }

    fun unsubscribe(destination: String) {
        val subscription = this.subscriptions[destination]

        if (subscription != null) {

            val (id) = subscription
            val event = Event(Command.UNSUBSCRIBE, mapOf("id" to "sub-${id}"))

            webSocket?.send(gson.toJson(event))
            this.subscriptions.remove(destination)
        }
    }

    fun disconnect() {
        webSocket?.close(1000, null)
    }

    fun isConnected(): Boolean {
        return isConnected
    }

    fun send(destination: String, body: Any? = null) {
        val header = mapOf("destination" to destination)
        val event = Event(Command.SEND, header, if (body is String) body else gson.toJson(body))

        webSocket?.send(gson.toJson(event))
    }
}
