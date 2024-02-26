package com.bitaqaty.currencyapp.presentation.convertCurrency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitaqaty.currencyapp.data.di.networkModule.IODispatcher
import com.bitaqaty.currencyapp.domain.usecase.ConvertUseCase
import com.bitaqaty.currencyapp.utils.Resource
import com.bitaqaty.currencyapp.data.remote.dto.ConvertResponse
import com.bitaqaty.currencyapp.data.remote.dto.SymbolsResponse
import com.bitaqaty.currencyapp.domain.usecase.GetSymbolsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor(
    private val convertUseCase: ConvertUseCase,
    private val getSymbolsUseCase: GetSymbolsUseCase,
@IODispatcher dispatcher: CoroutineDispatcher
    ) :
    ViewModel() {
    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(viewModelJob+dispatcher)
    private val _convert = MutableSharedFlow<Resource<ConvertResponse>>()
    val convert: MutableSharedFlow<Resource<ConvertResponse>>
        get() = _convert

    private val _symbolsSeries = MutableSharedFlow<Resource<SymbolsResponse>>()
    val symbolsSeries: MutableSharedFlow<Resource<SymbolsResponse>>
        get() = _symbolsSeries

    private val _symbolsSeries1 = MutableStateFlow<Resource<SymbolsResponse>?>(null)
    val symbolsSeries1: MutableStateFlow<Resource<SymbolsResponse>?>
        get() = _symbolsSeries1

    fun convert(from: String, to: String, amount: String) {
        uiScope.launch {
            convertUseCase.convert(from = from, to = to, amount = amount).let {
                _convert.emit(it)
            }
        }
    }

    fun getSymbols() {
        uiScope.launch {
            getSymbolsUseCase.getSymbolsSeries().let {
                _symbolsSeries.emit(it)
                _symbolsSeries1.value =it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}