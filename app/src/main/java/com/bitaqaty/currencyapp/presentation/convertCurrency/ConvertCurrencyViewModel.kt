package com.bitaqaty.currencyapp.presentation.convertCurrency

import androidx.lifecycle.ViewModel
import com.bitaqaty.currencyapp.domain.usecase.ConvertUseCase
import com.bitaqaty.currencyapp.utils.Resource
import com.bitaqaty.currencyapp.data.remote.dto.ConvertResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor(private val convertUseCase: ConvertUseCase) :
    ViewModel() {
    private val _convert = MutableStateFlow<Resource<ConvertResponse>?>(null)
    val convert: MutableStateFlow<Resource<ConvertResponse>?>
        get() = _convert

    private val viewModelJob = Job()
    // Since uiScope has a default dispatcher of Dispatchers.Main, this coroutine will be launched
    // in the main thread.
    val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    fun convert(from: String, to: String, amount: String) {
        uiScope.launch(Dispatchers.IO) {
            convertUseCase.convert(from = from, to = to, amount = amount).let {
                _convert.emit(it)
            }
        }
    }
}