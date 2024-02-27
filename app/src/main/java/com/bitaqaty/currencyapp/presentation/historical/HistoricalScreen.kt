package com.bitaqaty.currencyapp.presentation.historical

import android.icu.text.DecimalFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bitaqaty.currencyapp.R
import com.bitaqaty.currencyapp.data.remote.dto.Currency
import com.bitaqaty.currencyapp.data.remote.dto.CurrencyRate
import com.bitaqaty.currencyapp.presentation.convertCurrency.Loader
import com.bitaqaty.currencyapp.utils.ComposableWithSnackbar
import com.bitaqaty.currencyapp.utils.extention.getMap
import com.bitaqaty.currencyapp.utils.getCurrentDate
import com.bitaqaty.currencyapp.utils.getDateBeforeToDay
import com.bitaqaty.currencyapp.utils.handleLoadAndError
import com.bitaqaty.currencyapp.utils.networkConnection.ConnectionState
import com.bitaqaty.currencyapp.utils.networkConnection.connectivityState
import kotlinx.coroutines.delay

@Composable
fun HistoricalFragment(
    navController: NavController, currencyFrom: String, currencyTo: String,
    amountFrom: String, amountTo: String
) {
    val historicalViewModel = hiltViewModel<HistoricalViewModel>()
    val currencies by remember { mutableStateOf(ArrayList<Currency>()) }
    val currenciesRate by remember { mutableStateOf(ArrayList<CurrencyRate>()) }
    val snackbarVisibleState = remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf("") }
    val isLoader = remember { mutableStateOf(false) }
    val df = DecimalFormat("#.0000")
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available
    if (isConnected.not()) {
        messageError = stringResource(R.string.network_error)
        snackbarVisibleState.value = true
    } else {
        LaunchedEffect(true) {
            isLoader.value = true
            historicalViewModel.getCurrency(getCurrentDate(), currencyFrom)

            historicalViewModel.currency.collect {
                handleLoadAndError(it, snackbarVisibleState, isLoader) {
                    it.rates?.let { it1 ->
                        addCurrenciesListAndNotifyData(
                            it1,
                            currenciesRate,
                            df,
                            amountFrom
                        )
                    }
                }
            }
        }
        LaunchedEffect("timeSeries") {

            delay(1000)
            isLoader.value = true

            historicalViewModel.getTimeSeries(
                    startDate = getDateBeforeToDay(),
                    endDate = getCurrentDate(),
                    base = currencyFrom
                )
                historicalViewModel.timeSeries.collect { it ->
                    handleLoadAndError(it, snackbarVisibleState, isLoader) {
                        addTimeSeriesListAndNotifyData(
                            currencies, getMap(it.rates),
                            df,
                            currencyTo,
                            amountFrom.toDouble()
                        )
                    }
                }

        }
    }







    Row(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$amountFrom $currencyFrom",
                modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp),
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.last_3_days),
                modifier = Modifier.padding(top = 32.dp, start = 24.dp, end = 24.dp),
                textAlign = TextAlign.Start
            )

            TimeSeriesList(currencies)
        }

        Box(
            modifier = Modifier
                .requiredWidth(3.dp)
                .fillMaxHeight()
                .padding(start = 16.dp, end = 16.dp)
                .background(Color.Black)

        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$amountFrom $currencyFrom",
                modifier = Modifier.padding(top = 24.dp),
                textAlign = TextAlign.Start
            )
            CurrenciesList(currenciesRate = currenciesRate)
        }

    }
    ComposableWithSnackbar(snackbarVisibleState, messageError)

    Loader(isLoading = isLoader.value)

}


@Composable
fun TimeSeriesList(currencies: ArrayList<Currency>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        items(currencies) { currency ->
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val (txtTitle, view, txtValue) = createRefs()
                Text(
                    text = currency.date,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .constrainAs(txtTitle) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(start = 2.dp, top = 4.dp, bottom = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .constrainAs(view) {
                            start.linkTo(txtTitle.end)
                            end.linkTo(txtValue.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .requiredHeight(1.dp)
                        .padding(start = 2.dp, end = 2.dp)
                        .background(Color.Black)

                )

                Text(
                    text = currency.currencyValue.toString(),
                    fontSize = 11.sp,
                    modifier = Modifier
                        .constrainAs(txtValue) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(top = 4.dp, bottom = 4.dp)

                )
            }
        }
    }
}

@Composable
fun CurrenciesList(currenciesRate: List<CurrencyRate>) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        items(currenciesRate) { currency ->

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val (tvCurrencyName, tvCurrencyValue) = createRefs()
                Text(
                    text = currency.currencyName,
                    modifier = Modifier
                        .constrainAs(tvCurrencyName) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(5.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 11.sp
                )

                Text(
                    text = currency.currencyValue,
                    modifier = Modifier
                        .constrainAs(tvCurrencyValue) {
                            end.linkTo(parent.end)
                            top.linkTo(tvCurrencyName.top)
                            bottom.linkTo(tvCurrencyName.bottom)
                        },
                    textAlign = TextAlign.End,
                    fontSize = 11.sp
                )
            }
        }
    }
}


private fun addTimeSeriesListAndNotifyData(
    currencies: ArrayList<Currency>,
    map: Map<String, Any>?,
    df: DecimalFormat,
    currencyTo: String,
    amount: Double
) {
    map?.forEach { map ->
        val date = map.key
        getMap(map.value)?.forEach { it1 ->
            Log.e("ddd", currencyTo)
            Log.e("ddd", it1.key)
            if (it1.key == currencyTo) {
                currencies.add(
                    Currency(
                        date,
                        df.format(
                            (it1.value.toString().toDouble() * amount)
                        )
                    )
                )
            }
        }
    }
}


private fun addCurrenciesListAndNotifyData(
    rates: Any,
    currenciesRate: ArrayList<CurrencyRate>,
    df: DecimalFormat,
    amountFrom: String
) {
    getMap(rates)?.let {
        for (item in 0 until 10) {
            currenciesRate.add(
                CurrencyRate(
                    it.toList()[item].first,
                    df.format(
                        (it.toList()[item].second.toString().toBigDecimal().toDouble() *
                                amountFrom.toDouble())
                    )
                )
            )
        }
    }
}


