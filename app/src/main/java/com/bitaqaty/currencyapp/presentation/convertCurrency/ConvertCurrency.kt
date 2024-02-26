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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.bitaqaty.currencyapp.data.remote.dto.Currency

import com.bitaqaty.currencyapp.presentation.navigation.Screen
import com.bitaqaty.currencyapp.utils.extention.getMap

@Composable
fun ConvertCurrency(navController: NavController) {
    val convertCurrencyViewModel = hiltViewModel<ConvertCurrencyViewModel>()
    var fromText by remember { mutableStateOf("EUR") }
    var toText by remember { mutableStateOf("EGP") }
    var amountfrom by remember { mutableStateOf("") }
    var amountTo by remember { mutableStateOf("") }
    var isFromDropdownExpanded by remember { mutableStateOf(false) }
    var isToDropdownExpanded by remember { mutableStateOf(false) }
    val dropdownValues2 by remember { mutableStateOf(ArrayList<String>()) }

    LaunchedEffect(true) {
        // convertCurrencyViewModel.convert(from = "", to = "", amount = "12")
        //  convertCurrencyViewModel.getSymbols()

    }

    getMap(convertCurrencyViewModel.symbolsSeries.collectAsState().value?.data?.symbols)?.keys?.let {
        dropdownValues2.addAll(
            it
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Navigation(navController)
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
//                        makeConvert(fromText,toText, onClick = {
//                            convertCurrencyViewModel.convert(fromText,toText,"2")
//                        })
                    }
            ) {
                Text(
                    text = fromText.ifEmpty { "From" },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                DropdownMenu(
                    expanded = isFromDropdownExpanded,
                    onDismissRequest = { isFromDropdownExpanded = false },
                ) {
                    dropdownValues2.forEach { label ->
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            fromText = label
                            isFromDropdownExpanded = false
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
                Text(
                    text = toText.ifEmpty { "To" },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                DropdownMenu(
                    expanded = isToDropdownExpanded,
                    onDismissRequest = { isToDropdownExpanded = false },
                ) {
                    dropdownValues2.forEach { label ->
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            toText = label
                            isToDropdownExpanded = false
                            makeConvert(fromText, toText, onClick = {
                                convertCurrencyViewModel.convert(fromText, toText, "2")
                            })
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
                value = amountfrom,
                onValueChange = { amountfrom = it },
                label = { Text("From", textAlign = TextAlign.Center) },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp, end = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = amountTo,
                onValueChange = {
                    amountTo = it
                },
                label = { Text("To", textAlign = TextAlign.Center) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp, start = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                )


        }

        Button(
            onClick = {
                navController.navigate(
                    Screen.Historical.route
                        .plus("/${fromText}/${toText}/${amountfrom}/${amountTo}")
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Details")
        }
    }


}

fun makeConvert(fromText: String, toText: String, onClick: () -> Unit) {
    if (fromText != "From" && toText != "To") {
        onClick()
    } else {
        //   Toasty.error(requireContext(), getString(R.string.choose_currency)).show()
    }
}
//@Preview
//@Composable
//fun PreviewConvertCurrencyScreen() {
//    ConvertCurrency()
//}
