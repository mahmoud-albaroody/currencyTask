package com.bitaqaty.currencyapp

import androidx.compose.runtime.collectAsState
import com.bitaqaty.currencyapp.data.remote.APIs
import com.bitaqaty.currencyapp.data.remote.ApiRepoImpl
import com.bitaqaty.currencyapp.data.remote.dto.ConvertResponse
import com.bitaqaty.currencyapp.data.remote.dto.CurrencyResponse
import com.bitaqaty.currencyapp.data.remote.dto.SymbolsResponse
import com.bitaqaty.currencyapp.data.remote.dto.SymbolsResponsel
import com.bitaqaty.currencyapp.data.remote.dto.TimeSeriesResponse
import com.bitaqaty.currencyapp.domain.usecase.ConvertUseCase
import com.bitaqaty.currencyapp.domain.usecase.GetSymbolsUseCase
import com.bitaqaty.currencyapp.presentation.convertCurrency.ConvertCurrencyViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ConvertCurrencyViewModelTest {


    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    lateinit var viewModel: ConvertCurrencyViewModel

    @Before
    fun setup() {
        viewModel = getConvertCurrencyViewModel()
    }


    @Test
    fun test_result() {
        scope.runTest {
            val job = launch {
                viewModel.symbolsSeries.collect {
                    it.data?.success?.let { it1 -> assert(it1) }
                }
            }
            job.cancelAndJoin()
        }
    }


    private fun getConvertCurrencyViewModel(): ConvertCurrencyViewModel {
        val repo = ApiRepoImpl(TestApiRepo())
        val convertUseCase = ConvertUseCase(repo)
        val getSymbolsUseCase = GetSymbolsUseCase(repo)
        return ConvertCurrencyViewModel(convertUseCase, getSymbolsUseCase, dispatcher)
    }
}

class TestApiRepo : APIs {
    override suspend fun timeSeries(
        token: String,
        start_date: String,
        end_date: String,
        base: String
    ): Response<TimeSeriesResponse> {
        return SymbolsResponsel.getTimeSeriesResponse()
    }

    override suspend fun convert(
        token: String,
        to: String,
        from: String,
        amount: String
    ): Response<ConvertResponse> {
        return SymbolsResponsel.getConvertResponse()
    }

    override suspend fun symbols(token: String): Response<SymbolsResponse> {
        return SymbolsResponsel.getSymbolsResponse()
    }

    override suspend fun getCurrencyData(
        date: String,
        access_key: String,
        base: String
    ): Response<CurrencyResponse> {
        return SymbolsResponsel.getCurrencyResponse()
    }

}