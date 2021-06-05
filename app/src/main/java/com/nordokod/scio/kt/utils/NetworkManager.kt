package com.nordokod.scio.kt.utils

import com.nordokod.scio.kt.constants.Generic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.*
import kotlin.Exception

class NetworkManager {
    companion object {
        suspend fun isOnline() : Boolean{
            val sock = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)
            return try{
                withContext(Dispatchers.IO) {
                    sock.connect(socketAddress, Generic.PING_TIME)
                    sock.close()
                    delay(1000)
                    true
                }
            } catch ( e : Exception){
                false
            }
        }
    }
}