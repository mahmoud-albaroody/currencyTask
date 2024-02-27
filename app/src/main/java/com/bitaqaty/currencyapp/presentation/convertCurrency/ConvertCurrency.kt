package com.bitaqaty.currencyapp.presentation.convertCurrency


import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*

import androidx.compose.ui.unit.dp


import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ConvertCurrency() {
    val convertCurrencyViewModel = hiltViewModel<ConvertCurrencyViewModel>()
    val convert = convertCurrencyViewModel.convert
    val progressBar = remember { mutableStateOf(false) }
    var fromText by remember { mutableStateOf("") }
    var toText by remember { mutableStateOf("") }
    var isFromDropdownExpanded by remember { mutableStateOf(false) }
    var isToDropdownExpanded by remember { mutableStateOf(false) }
    val dropdownValues =
        listOf("Item 1", "Item 2", "Item 3") // Replace with your actual dropdown values

    LaunchedEffect(true) {
       // convertCurrencyViewModel.convert(from = "", to = "", amount = "12")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Box(modifier = Modifier.size(150.dp)) {
                Text(
                    text = if (fromText.isEmpty()) "From" else fromText,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable { isFromDropdownExpanded = true }
                )

                DropdownMenu(
                    expanded = isFromDropdownExpanded,
                    onDismissRequest = { isFromDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    dropdownValues.forEach { label ->
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            fromText = label
                            isFromDropdownExpanded = false
                        })
                    }
                }
            }
            TextField(
                value = fromText,
                onValueChange = { fromText = it },
                label = { Text("From") },
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row {

            TextField(
                value = toText,
                onValueChange = { toText = it },
                label = { Text("To") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (toText.isEmpty()) "To" else toText,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable { isToDropdownExpanded = true }
                )

                DropdownMenu(
                    expanded = isToDropdownExpanded,
                    onDismissRequest = { isToDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    dropdownValues.forEach { label ->
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            toText = label
                            isToDropdownExpanded = false
                        })


                    }
                }
            }

        }

        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Details")
        }
    }
}


@Preview
@Composable
fun PreviewConvertCurrencyScreen() {
    ConvertCurrency()
}
