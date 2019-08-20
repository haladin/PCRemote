package com.robobender.pcremote.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robobender.pcremote.data.Message
import com.robobender.pcremote.network.NetworkClient
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var client: NetworkClient? = null

    init {
        client = NetworkClient.networkClientFactory("websocket")
    }

    fun connect(serverURL: String){
        viewModelScope.launch  {
            client?.connect(serverURL)
        }
    }

    fun disconnect(){
        viewModelScope.launch  {
            client?.disconnect()
        }
    }

    fun send(message: Message){
        viewModelScope.launch  {
            client?.send(message)
        }
    }
}