package com.bitaqaty.currencyapp.presentation.convertCurrency

import androidx.lifecycle.ViewModel
import com.bitaqaty.currencyapp.domain.usecase.ConvertUseCase
import com.bitaqaty.currencyapp.utils.Resource
import com.bitaqaty.currencyapp.data.remote.dto.ConvertResponse
import com.bitaqaty.currencyapp.data.remote.dto.SymbolsResponse
import com.bitaqaty.currencyapp.domain.usecase.GetSymbolsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor(
    private val convertUseCase: ConvertUseCase,
    private val getSymbolsUseCase: GetSymbolsUseCase
) :
    ViewModel() {
    private val viewModelJob = Job()

    // Since uiScope has a default dispatcher of Dispatchers.Main, this coroutine will be launched
    // in the main thread.
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val _convert = MutableStateFlow<Resource<ConvertResponse>?>(null)
    val convert: MutableStateFlow<Resource<ConvertResponse>?>
        get() = _convert

    private val _symbolsSeries = MutableStateFlow<Resource<SymbolsResponse>?>(null)
    val symbolsSeries: MutableStateFlow<Resource<SymbolsResponse>?>
        get() = _symbolsSeries

    fun convert(from: String, to: String, amount: String) {
        uiScope.launch(Dispatchers.IO) {
            convertUseCase.convert(from = from, to = to, amount = amount).let {
                _convert.emit(it)
            }
        }
    }

    fun getSymbols() {
        uiScope.launch(Dispatchers.IO) {
            getSymbolsUseCase.getSymbolsSeries().let {
                _symbolsSeries.emit(it)
            }
        }
    }
}