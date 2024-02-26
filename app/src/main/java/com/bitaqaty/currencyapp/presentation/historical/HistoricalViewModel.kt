package com.bitaqaty.currencyapp.presentation.historical

import androidx.lifecycle.ViewModel
import com.bitaqaty.currencyapp.data.remote.dto.CurrencyResponse
import com.bitaqaty.currencyapp.data.remote.dto.TimeSeriesResponse
import com.bitaqaty.currencyapp.domain.usecase.RateUseCase
import com.bitaqaty.currencyapp.domain.usecase.TimeSeriesUseCase
import com.bitaqaty.currencyapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalViewModel @Inject constructor(
    private val timeSeriesUseCase: TimeSeriesUseCase,
    private val rateUseCase: RateUseCase
) : ViewModel() {
    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private val _timeSeries = MutableStateFlow<Resource<TimeSeriesResponse>?>(null)
    val timeSeries: MutableStateFlow<Resource<TimeSeriesResponse>?>
        get() = _timeSeries

    private val _currency = MutableStateFlow<Resource<CurrencyResponse>?>(null)
    val currency: MutableStateFlow<Resource<CurrencyResponse>?>
        get() = _currency

    fun getTimeSeries(
        startDate: String,
        endDate: String,
        base: String
    ) {
        uiScope.launch {
            timeSeriesUseCase.getTimeSeries(startDate = startDate, endDate = endDate, base = base)
                .let {
                    _timeSeries.emit(it)
                }
        }
    }

    fun getCurrency(
        date: String,
        base: String
    ) {
        uiScope.launch(Dispatchers.IO) {
            rateUseCase.getCurrencyRate(date = date, base = base)
                .let {
                    _currency.emit(it)
                }
        }
    }


    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}

