package com.bitaqaty.currencyapp.presentation.convertCurrency


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.bitaqaty.currencyapp.R
import com.bitaqaty.currencyapp.presentation.navigation.Screen
import com.bitaqaty.currencyapp.utils.ComposableWithSnackbar
import com.bitaqaty.currencyapp.utils.extention.getMap
import com.bitaqaty.currencyapp.utils.handleLoadAndError
import com.bitaqaty.currencyapp.utils.networkConnection.ConnectionState
import com.bitaqaty.currencyapp.utils.networkConnection.connectivityState

import kotlinx.coroutines.delay


@Composable
fun ConvertCurrency(navController: NavController) {
    val convertCurrencyViewModel = hiltViewModel<ConvertCurrencyViewModel>()
    var fromCurrency by remember { mutableStateOf("From") }
    var toCurrency by remember { mutableStateOf("To") }
    var fromAmount by remember { mutableStateOf("") }
    var toAmount by remember { mutableStateOf("") }
    var callConvert by remember { mutableStateOf("") }
    var isFromDropdownExpanded by remember { mutableStateOf(false) }
    var isToDropdownExpanded by remember { mutableStateOf(false) }
    val snackbarVisibleState = remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf("") }
    val isLoader = remember { mutableStateOf(false) }
    val dropdownValues by remember { mutableStateOf(ArrayList<String>()) }
    val from = stringResource(R.string.from)
    val to = stringResource(R.string.to)
    val connection by connectivityState()
    var isSwap by remember { mutableStateOf(false) }
    val isConnected = connection === ConnectionState.Available
    if (isConnected.not()) {
        messageError = stringResource(R.string.network_error)
        snackbarVisibleState.value = true
    } else {
        snackbarVisibleState.value = false
        LaunchedEffect(true) {
            isLoader.value = true
            convertCurrencyViewModel.getSymbols()
            convertCurrencyViewModel.symbolsSeries.collect {
                handleLoadAndError(it, snackbarVisibleState, isLoader) {
                    it.symbols?.let { it1 -> setCurrenciesToSpinner(dropdownValues, it1) }
                }
            }
        }
        LaunchedEffect(callConvert) {
            if (callConvert.isNotEmpty()) {
                delay(2000)
                if (isSwap) {
                    makeConvert(
                        toCurrency,
                        fromCurrency,
                        toAmount,
                        convertCurrencyViewModel,
                        isLoader,
                        snackbarVisibleState, to, from
                    )
                } else {
                    makeConvert(
                        fromCurrency,
                        toCurrency,
                        fromAmount,
                        convertCurrencyViewModel,
                        isLoader,
                        snackbarVisibleState, to, from
                    )
                }

                convertCurrencyViewModel.convert.collect {

                    handleLoadAndError(it, snackbarVisibleState, isLoader) {
                        if (isSwap) {
                            fromAmount = it.result.toString()
                        } else {
                            toAmount = it.result.toString()
                        }
                    }
                }

            }
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp, top = 16.dp, end = 16.dp)
                    .background(Color.Yellow, shape = RoundedCornerShape(16.dp))
                    .clickable {
                        isFromDropdownExpanded = true
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = fromCurrency.ifEmpty { "From" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .padding(16.dp)
                    )
                    Icon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),

                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = isFromDropdownExpanded,
                    onDismissRequest = { isFromDropdownExpanded = false },

                    ) {
                    dropdownValues.forEach { label ->
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            fromCurrency = label
                            isFromDropdownExpanded = false
                            callConvert = label
                        })
                    }
                }

            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp, top = 16.dp, start = 16.dp)
                    .background(Color.Yellow, shape = RoundedCornerShape(16.dp))
                    .clickable {
                        isToDropdownExpanded = true

                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = toCurrency.ifEmpty { "To" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .padding(16.dp)
                    )
                    Icon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),

                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = isToDropdownExpanded,
                    onDismissRequest = { isToDropdownExpanded = false },
                ) {
                    dropdownValues.forEach { label ->
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            toCurrency = label
                            isToDropdownExpanded = false
                            callConvert = label
                        })
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = fromAmount,
                onValueChange = {
                    fromAmount = it
                    isSwap = false
                    callConvert = fromAmount
                },

                label = { Text(stringResource(R.string.from), textAlign = TextAlign.Center) },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp, end = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = toAmount,
                onValueChange = {
                    isSwap = true
                    toAmount = it
                    callConvert = toAmount
                },
                label = { Text(stringResource(R.string.to), textAlign = TextAlign.Center) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp, start = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                )


        }

        Button(
            onClick = {
                if(fromAmount.isNotEmpty()&&toAmount.isNotEmpty()) {

                    navController.navigate(
                        Screen.Historical.route
                            .plus("/${fromCurrency}/${toCurrency}/${fromAmount}/${toAmount}")
                    )
                }else{snackbarVisibleState.value = true
                    messageError = "Empty value"}
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.details))
        }
    }

    ComposableWithSnackbar(snackbarVisibleState, messageError)

    Loader(isLoading = isLoader.value)
}


private fun makeConvert(
    fromCurrency: String,
    toCurrency: String,
    fromAmount: String,
    convertCurrencyViewModel: ConvertCurrencyViewModel,
    isLoader: MutableState<Boolean>,
    snackbarVisibleState: MutableState<Boolean>, to: String, from: String
) {
    if (fromCurrency != from && toCurrency != to && fromAmount.isNotEmpty()) {
        isLoader.value = true
        convertCurrencyViewModel.convert(
            fromCurrency,
            toCurrency,
            fromAmount
        )
    } else {
        //snackbarVisibleState.value = true
    }
}


private fun setCurrenciesToSpinner(
    dropdownValues2: ArrayList<String>, symbols: Any
) {
    getMap(symbols)?.keys?.let { dropdownValues2.addAll(it) }
}


@Composable
fun Loader(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

//@Preview
//@Composable
//fun PreviewConvertCurrencyScreen() {
//    ConvertCurrency()
//}
