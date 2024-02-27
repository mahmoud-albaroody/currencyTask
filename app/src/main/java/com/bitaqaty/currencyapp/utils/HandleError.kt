package com.bitaqaty.currencyapp.utils

enum class HandleError(val value: Int) {
    AUTH(401),
    ERRORPARAMTER(400),
    NOTFOUNDD(404),
    CRACH(500),
    ExpierAPITOKEN(429)

}
