package com.bitaqaty.currencyapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bitaqaty.currencyapp.R
import com.bitaqaty.currencyapp.presentation.convertCurrency.ConvertCurrency
import com.bitaqaty.currencyapp.presentation.historical.HistoricalFragment

@Composable
fun Navigation(
    navController: NavHostController, modifier: Modifier? = null
) {
    NavHost(navController, startDestination = Screen.ConvertCurrency.route) {
        composable(Screen.ConvertCurrency.route) {
            ConvertCurrency(navController)
        }

        composable(
            Screen.Historical.route.plus("/{fromText}/{toText}/{amountfrom}/{amountTo}")
        ) {
            val fromText = it.arguments?.getString("fromText") ?: ""
            val toText = it.arguments?.getString("toText") ?: ""
            val amountfrom = it.arguments?.getString("amountfrom") ?: ""
            val amountTo = it.arguments?.getString("amountTo") ?: ""
            HistoricalFragment(navController, fromText, toText, amountfrom, amountTo)

        }
    }
}

@Composable
fun navigationTitle(navController: NavController): String {
    return when (currentRoute(navController)) {
        Screen.ConvertCurrency.route -> stringResource(id = R.string.app_name)
        Screen.Historical.route -> stringResource(id = R.string.app_name)
        else -> {
            ""
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}
