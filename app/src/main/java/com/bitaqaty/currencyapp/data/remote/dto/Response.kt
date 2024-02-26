package com.bitaqaty.currencyapp.data.remote.dto

import android.icu.text.IDNA
import com.google.gson.annotations.SerializedName
import retrofit2.Response

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

object SymbolsResponsel {
    fun getSymbolsResponse(): Response<SymbolsResponse> {
        return Response.success(SymbolsResponse(false, ""))
    }

    fun getConvertResponse(): Response<ConvertResponse> {
        return Response.success(
            ConvertResponse(
                "", null,
                null, 12.0, true
            )
        )
    }

    fun getCurrencyResponse(): Response<CurrencyResponse> {
        return Response.success(CurrencyResponse(true))
    }

    fun getTimeSeriesResponse(): Response<TimeSeriesResponse> {
        return Response.success(TimeSeriesResponse("EGP", success = true, timeseries = true))
    }
}


data class ConvertResponse(
    val date: String,
    val info: Info?,
    val query: Query?,
    val result: Double,
    val success: Boolean
) : StatusResponse()

data class TimeSeriesResponse(
    val base: String? = null,
    val end_date: String? = null,
    val rates: Any? = null,
    val start_date: String? = null,
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
