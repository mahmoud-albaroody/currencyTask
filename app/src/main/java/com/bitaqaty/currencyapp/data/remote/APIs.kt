package com.bitaqaty.currencyapp.data.remote

import com.bitaqaty.currencyapp.data.remote.dto.ConvertResponse
import com.bitaqaty.currencyapp.data.remote.dto.CurrencyResponse
import com.bitaqaty.currencyapp.data.remote.dto.SymbolsResponse
import com.bitaqaty.currencyapp.data.remote.dto.TimeSeriesResponse
import com.bitaqaty.currencyapp.utils.constants.Constants.ACCESS_TOKEN
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Query


interface APIs {

    @GET("timeseries")
    suspend fun timeSeries(
        @Header("apikey") token: String = ACCESS_TOKEN,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("base") base: String
    ): Response<TimeSeriesResponse>

    @GET("convert")
    suspend fun convert(
        @Header("apikey") token: String = ACCESS_TOKEN,
        @Query("to") to: String,
        @Query("from") from: String,
        @Query("amount") amount: String
    ): Response<ConvertResponse>


    @GET("symbols")
    suspend fun symbols(
        @Header("apikey") token: String = ACCESS_TOKEN,
    ): Response<SymbolsResponse>

    @GET("{date}")
    suspend fun getCurrencyData(
        @Path("date") date: String,
        @Query("apikey") access_key: String = ACCESS_TOKEN,
        @Query("base") base: String
    ): Response<CurrencyResponse>

}