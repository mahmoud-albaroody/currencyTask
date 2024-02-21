package com.bitaqaty.currencyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

open class StatusResponse(
    @SerializedName("isSuccessed")
    val isSuccessed: Boolean = false,

    @SerializedName("error")
    val error: String? = ""
)

data class SymbolsResponse(
    val success: Boolean = false,
    var symbols: Any? = null
) : StatusResponse()

data class ConvertResponse(
    val date: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
) : StatusResponse()

data class TimeSeriesResponse(
    val base: String,
    val end_date: String,
    val rates: Any,
    val start_date: String,
    val success: Boolean,
    val timeseries: Boolean
) : StatusResponse()

data class CurrencyResponse(
    var success: Boolean? = null,
    var timestamp: Long? = null,
    var historical: Boolean? = null,
    var base: String? = null,
    var date: String? = null,
    var rates: Any? = null
) : StatusResponse()
