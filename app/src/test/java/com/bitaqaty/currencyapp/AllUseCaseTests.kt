package com.bitaqaty.currencyapp

import com.bitaqaty.currencyapp.data.remote.ApiRepoImpl
import com.bitaqaty.currencyapp.domain.usecase.ConvertUseCase
import com.bitaqaty.currencyapp.domain.usecase.GetSymbolsUseCase
import com.bitaqaty.currencyapp.domain.usecase.RateUseCase
import com.bitaqaty.currencyapp.domain.usecase.TimeSeriesUseCase
import com.bitaqaty.currencyapp.utils.getCurrentDate
import com.bitaqaty.currencyapp.utils.getDateBeforeToDay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetSymbolsUseCaseTest {

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun get_symbols() = scope.runTest {
        val repo = ApiRepoImpl(TestApiRepo())
        val getSymbolsUseCaseTest = GetSymbolsUseCase(repo)
        getSymbolsUseCaseTest.getSymbolsSeries()?.data?.success?.let { assert(it) }
    }

    @Test
    fun get_convert() = scope.runTest {
        val repo = ApiRepoImpl(TestApiRepo())
        val convertUseCase = ConvertUseCase(repo)
        convertUseCase.convert("EGP", "EUR", "12")?.data?.success?.let { assert(it) }
    }

    @Test
    fun rate_UseCase() = scope.runTest {
        val repo = ApiRepoImpl(TestApiRepo())
        val rateUseCase = RateUseCase(repo)
        rateUseCase.getCurrencyRate(getCurrentDate(), "EUR")?.data?.success?.let { assert(it) }
    }

    @Test
    fun time_series_UseCase() = scope.runTest {
        val repo = ApiRepoImpl(TestApiRepo())
        val timeSeriesUseCase = TimeSeriesUseCase(repo)
        timeSeriesUseCase.getTimeSeries(
            getDateBeforeToDay(),
            getCurrentDate(),
            "12"
        )?.data?.success?.let { assert(it) }
    }
}