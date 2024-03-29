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

    private val _timeSeries = MutableSharedFlow<Resource<TimeSeriesResponse>>()
    val timeSeries: MutableSharedFlow<Resource<TimeSeriesResponse>>
        get() = _timeSeries

    private val _currency = MutableSharedFlow<Resource<CurrencyResponse>>()
    val currency: MutableSharedFlow<Resource<CurrencyResponse>>
        get() = _currency

    fun getTimeSeries(
        startDate: String,
        endDate: String,
        base: String
    ) {
        uiScope.launch {
            timeSeriesUseCase.getTimeSeries(startDate = startDate, endDate = endDate, base = base)
                .let {
                    it?.let { it1 -> _timeSeries.emit(it1) }
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
                    it?.let { it1 -> _currency.emit(it1) }
                }
        }
    }


    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}

