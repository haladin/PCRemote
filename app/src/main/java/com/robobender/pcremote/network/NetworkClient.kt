package com.robobender.pcremote.network

import com.robobender.pcremote.data.Message

interface NetworkClient {

    var isConnected: Boolean
    fun connect(serverURL: String)
    fun disconnect()
    fun send(message: Message)

    companion object {
        fun networkClientFactory(format: String) = when (format) {
            "websocket" -> WebSocketClient()
            else -> throw IllegalStateException("Unknown format")
        }
    }
}