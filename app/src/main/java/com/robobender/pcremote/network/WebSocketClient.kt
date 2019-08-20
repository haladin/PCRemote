package com.robobender.pcremote.network

import android.util.Log
import com.google.gson.Gson
import com.robobender.pcremote.data.Message
import okhttp3.*
import okio.ByteString

class WebSocketClient: NetworkClient {

    private var TAG = "WebSocketClient"

    var socket: OkHttpClient? = null
    var request: Request? = null
    var webSocketClient: WebSocket? = null
    override var isConnected = false

    private var webSocketListener = object : WebSocketListener() {

        val TAG = "WebSocketListener"
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.e(TAG, "Socket connected.")
            this@WebSocketClient.isConnected = true
        }

        override fun onMessage(webSocket: WebSocket, text:String) {
            Log.e(TAG, "MESSAGE: $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.e(TAG, "MESSAGE: ${bytes.hex()}")
        }

        override fun onClosing(webSocket: WebSocket, code:Int, reason:String) {
            webSocket.close(1000, null)
            webSocket.cancel()
            this@WebSocketClient.isConnected = false
            Log.e(TAG, "CLOSE: $code $reason")
        }

        override fun onClosed(webSocket: WebSocket, code:Int, reason:String) {
            //TODO: stuff
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            this@WebSocketClient.isConnected = false
            Log.e(TAG, "MESSAGE: $response")
            Log.e(TAG, "throwable: ${t.message}")
        }
    }

    override fun connect(serverURL: String){

        if (serverURL != "") {
            // val loggingInterceptor = HttpLoggingInterceptor()
            // loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            socket = OkHttpClient()
            request = Request.Builder().url(serverURL).build()
            webSocketClient = socket?.newWebSocket(request!!, webSocketListener)
        }
    }

    override fun disconnect() {
        socket?.dispatcher?.executorService?.shutdown()
    }

    override fun send(message: Message){
        val jsonString = Gson().toJson(message)
        Log.d(TAG, "$jsonString")
        Log.d(TAG, "${webSocketClient?.send(jsonString)}")
    }
}