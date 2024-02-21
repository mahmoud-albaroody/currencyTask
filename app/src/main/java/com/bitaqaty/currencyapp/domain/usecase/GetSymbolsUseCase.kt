package com.bitaqaty.currencyapp.domain.usecase


import com.bitaqaty.currencyapp.data.remote.ApiRepoImpl
import com.bitaqaty.currencyapp.data.remote.dto.SymbolsResponse
import com.bitaqaty.currencyapp.utils.Resource

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetSymbolsUseCase @Inject constructor(private val repository: ApiRepoImpl) {
    suspend fun getSymbolsSeries(): Resource<SymbolsResponse> {
        lateinit var result: Resource<SymbolsResponse>
        result { result = it }
        return result
    }

    private suspend fun result(
        res: (Resource<SymbolsResponse>) -> Unit
    ) {
        flow {
            emit(repository.symbols())
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