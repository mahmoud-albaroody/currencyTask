package com.bitaqaty.currencyapp.domain.usecase


import com.bitaqaty.currencyapp.data.remote.ApiRepoImpl
import com.bitaqaty.currencyapp.data.remote.dto.ConvertResponse
import com.bitaqaty.currencyapp.utils.Resource
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ConvertUseCase @Inject constructor(private val repository: ApiRepoImpl) {


    suspend fun convert(from: String, to: String, amount: String): Resource<ConvertResponse> {
        lateinit var result: Resource<ConvertResponse>
        result(from = from, to = to, amount = amount) { result = it }
        return result
    }

    private suspend fun result(
        from: String, to: String, amount: String,
        res: (Resource<ConvertResponse>) -> Unit
    ) {
        flow {
            emit(repository.convert(from = from, to = to, amount = amount))
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