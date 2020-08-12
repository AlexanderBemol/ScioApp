package com.nordokod.scio.kt.utils

import com.nordokod.scio.kt.constants.Generic
import java.net.InetAddress

class NetworkManager {
    companion object {
        fun isOnline(): Boolean {
            return try {
                !InetAddress.getByName(Generic.PING_SITE).equals("")
            } catch (e: Exception) {
                false
            }
        }
    }
}