package com.bitaqaty.currencyapp.domain.usecase

import com.bitaqaty.currencyapp.data.remote.ApiRepoImpl
import com.bitaqaty.currencyapp.data.remote.dto.TimeSeriesResponse
import com.bitaqaty.currencyapp.utils.Resource
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.*
import javax.inject.Inject

class TimeSeriesUseCase @Inject constructor(private val repository: ApiRepoImpl) {
    suspend fun getTimeSeries(
        startDate: String,
        endDate: String,
        base: String
    ): Resource<TimeSeriesResponse>? {
         var result: Resource<TimeSeriesResponse>?=null
        result(startDate = startDate, endDate = endDate, base = base) { result = it }
        return result
    }

     suspend fun result(
        startDate: String, endDate: String, base: String,
        res: (Resource<TimeSeriesResponse>) -> Unit
    ) {
        flow {
            emit(repository.timeSeries(startDate = startDate, endDate = endDate, base = base))
        }.flowOn(Dispatchers.IO)
            .catch {
                it.message?.let {
                    Resource.DataError(
                        null,
                        0, null
                    )
                }
            }
            .buffer().collect {
                res(it)
            }
    }
}