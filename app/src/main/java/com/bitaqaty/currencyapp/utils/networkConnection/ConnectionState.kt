package com.bitaqaty.currencyapp.utils.networkConnection

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}