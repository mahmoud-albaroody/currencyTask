package com.bitaqaty.currencyapp.data.remote

import com.bitaqaty.currencyapp.data.remote.dto.ConvertResponse
import com.bitaqaty.currencyapp.data.remote.dto.CurrencyResponse
import com.bitaqaty.currencyapp.data.remote.dto.SymbolsResponse
import com.bitaqaty.currencyapp.data.remote.dto.TimeSeriesResponse
import com.bitaqaty.currencyapp.utils.Resource

import javax.inject.Inject


class ApiRepoImpl @Inject constructor(private val APIs: APIs) {
     suspend fun timeSeries(
        startDate: String,
        endDate: String,
        base: String
    ): Resource<TimeSeriesResponse> {
        val response = APIs.timeSeries(start_date = startDate, end_date = endDate, base = base)
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!, response.errorBody())
        } else {
            Resource.DataError(null, response.code(), response.errorBody())
        }
    }

    suspend fun convert(
        from: String,
        to: String,
        amount: String
    ): Resource<ConvertResponse> {
        val response = APIs.convert(from = from, to = to, amount = amount)
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!, response.errorBody())
        } else {
            Resource.DataError(null, response.code(), response.errorBody())
        }
    }

     suspend fun symbols(): Resource<SymbolsResponse> {
        val response = APIs.symbols()
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!, response.errorBody())
        } else {
            Resource.DataError(null, response.code(), response.errorBody())
        }
    }

     suspend fun getCurrencyData(date: String, base: String): Resource<CurrencyResponse> {
        val response = APIs.getCurrencyData(date = date, base = base)
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!, response.errorBody())
        } else {
            Resource.DataError(null, response.code(), response.errorBody())
        }
    }


}
