package com.bitaqaty.currencyapp.domain.usecase

import com.bitaqaty.currencyapp.data.remote.ApiRepoImpl
import com.bitaqaty.currencyapp.data.remote.dto.CurrencyResponse
import com.bitaqaty.currencyapp.utils.Resource
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RateUseCase @Inject constructor(private val repository: ApiRepoImpl) {
    suspend fun getCurrencyRate(date: String, base: String): Resource<CurrencyResponse>? {
         var result: Resource<CurrencyResponse>?=null
        result(date = date, base = base) { result = it }
        return result
    }

    private suspend fun result(
        date: String, base: String,
        res: (Resource<CurrencyResponse>) -> Unit
    ) {
        flow {
            emit(repository.getCurrencyData(date = date, base = base))
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