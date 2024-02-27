package com.bitaqaty.currencyapp.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ComposableWithSnackbar(snackbarVisibleState: MutableState<Boolean>, messageError:String) {
    if (snackbarVisibleState.value) {
        Snackbar(
            action = {
                Button(onClick = { snackbarVisibleState.value = false }) {
                    Text(text = "Dismiss")
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = messageError, color = Color.White)
        }
    }
}
