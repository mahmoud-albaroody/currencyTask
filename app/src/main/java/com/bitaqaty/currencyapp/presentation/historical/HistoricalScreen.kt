package com.bitaqaty.currencyapp.presentation.historical

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bitaqaty.currencyapp.data.remote.dto.Currency
import com.bitaqaty.currencyapp.data.remote.dto.CurrencyRate
import com.bitaqaty.currencyapp.utils.extention.getMap
import com.bitaqaty.currencyapp.utils.getCurrentDate
import com.bitaqaty.currencyapp.utils.getDateBeforeToDay

@Composable
fun HistoricalFragment(
    navController: NavController, currencyFrom: String, currencyTo: String,
    amountFrom: String, amountTo: String
) {
    val historicalViewModel = hiltViewModel<HistoricalViewModel>()
    val currencies: ArrayList<Currency> = arrayListOf()
    val currenciesRate: ArrayList<CurrencyRate> = arrayListOf()
    val df = DecimalFormat("#.00")
    LaunchedEffect(true) {
        historicalViewModel.getTimeSeries(
            startDate = getDateBeforeToDay(),
            endDate = getCurrentDate(),
            base = currencyFrom
        )
        historicalViewModel.getCurrency(getCurrentDate(), currencyFrom)
    }


    AddTimeSeriesListAndNotifyData(
        currencies, getMap(historicalViewModel.timeSeries.collectAsState().value?.data?.rates),
        df,
        currencyTo,
        amountFrom.toDouble()
    )

    AddCurrenciesListAndNotifyData(historicalViewModel, currenciesRate, df, amountFrom)

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
                text = "Last 3 days",
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
                    modifier = Modifier
                        .constrainAs(txtTitle) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(start = 4.dp, top = 8.dp, bottom = 8.dp)
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
                    modifier = Modifier
                        .constrainAs(txtValue) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CurrenciesList(currenciesRate: ArrayList<CurrencyRate>) {

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
                    fontSize = 9.sp
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
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun AddTimeSeriesListAndNotifyData(
    currencies: ArrayList<Currency>,
    map: Map<String, Any>?,
    df: DecimalFormat,
    currencyTo: String,
    amount: Double
) {
    map?.forEach {
        val date = it.key
        getMap(it.value)?.forEach {
            if (it.key == currencyTo) {
                currencies.add(
                    Currency(
                        date,
                        df.format(
                            (it.value.toString().toDouble() * amount)
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun AddCurrenciesListAndNotifyData(
    historicalViewModel: HistoricalViewModel,
    currenciesRate: ArrayList<CurrencyRate>,
    df: DecimalFormat,
    amountFrom: String
) {
    historicalViewModel.currency.collectAsState().value?.let { it1 ->
        getMap(it1.data?.rates)?.let {
            for (item in 0 until it.toList().size) {
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
}


