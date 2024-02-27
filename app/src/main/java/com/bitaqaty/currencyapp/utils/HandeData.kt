package com.bitaqaty.currencyapp.utils

import androidx.compose.runtime.MutableState
import com.bitaqaty.currencyapp.data.remote.dto.CurrencyResponse

fun <T> handleLoadAndError(
    v: Resource<T>,
    snackbarVisibleState: MutableState<Boolean>,
    isLoader: MutableState<Boolean>,
    resultCallBack: (T) -> Unit
) {
    when (v) {
        is Resource.Loading -> {
            isLoader.value = true
        }

        is Resource.Success -> {
            isLoader.value = false
            v.data?.let { resultCallBack.invoke(it) }

        }

        is Resource.DataError -> {
            isLoader.value = false
//            handleResponseError(v.errorBody)
            snackbarVisibleState.value = false
        }
    }
}
