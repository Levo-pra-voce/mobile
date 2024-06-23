package com.levopravoce.mobile.config

import com.google.gson.GsonBuilder
import com.levopravoce.mobile.BuildConfig
import com.levopravoce.mobile.features.app.data.dto.MessageSocketDTO
import com.levopravoce.mobile.features.auth.domain.AuthStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import java.net.SocketException
import javax.inject.Inject
import javax.inject.Singleton


data class WebSocketEventDTO(private val headers: Map<String, String>, private val body: String?) {
    enum class HEADERS(
        val value: String
    ) {
        DESTINATION("destination")
    }
}

enum class Destination(val value: String) {
    ORDER_MAP("/order-map"),
    ORDER_PAYMENT("/order-payment")
}

@Singleton
class WebSocketClient @Inject constructor(
    private val authStore: AuthStore
) {
    private var webSocket: WebSocket? = null
    private var isConnected: Boolean = false
    private var isClosed: Boolean = false
    private var isLoadingConnection: Boolean = false
    private val gson = GsonBuilder().serializeNulls().create();
    var callback: ((MessageSocketDTO) -> Unit)? = null

    val messageFlow = MutableStateFlow<MessageSocketDTO?>(null);

    private val webSocketListener: okhttp3.WebSocketListener = object : okhttp3.WebSocketListener(
    ) {
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            isConnected = false
            isLoadingConnection = false
            if (t is SocketException) {
                runBlocking(Dispatchers.IO) {
                    while (true) {
                        if (isConnected || isClosed) {
                            break
                        }
                        delay(1000)
                        if (!isLoadingConnection) {
                            connect()
                        }
                    }
                }
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            try {
                runBlocking(Dispatchers.IO) {
                    val message = gson.fromJson(text, MessageSocketDTO::class.java)
                    messageFlow.emit(message)
                    callback?.invoke(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            isConnected = false
            isLoadingConnection = false
            isClosed = true
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            isConnected = true
            isLoadingConnection = false
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
            isLoadingConnection = true
            isClosed = false
            this.webSocket = websocket
        }
    }

    fun disconnect() {
        webSocket?.close(1000, null)
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
