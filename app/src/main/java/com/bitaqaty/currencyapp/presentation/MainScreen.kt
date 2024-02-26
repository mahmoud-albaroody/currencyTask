package com.bitaqaty.currencyapp.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.bitaqaty.currencyapp.presentation.navigation.Navigation

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Navigation(navController = navController, Modifier.padding(8.dp))
    }
}