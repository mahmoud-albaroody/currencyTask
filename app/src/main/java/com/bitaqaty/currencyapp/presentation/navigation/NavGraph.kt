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
import com.bitaqaty.currencyapp.data.remote.dto.Info
import com.bitaqaty.currencyapp.presentation.convertCurrency.ConvertCurrency

@Composable
fun Navigation(
    navController: NavHostController, modifier: Modifier, genres: ArrayList<Info>? = null,
) {
    NavHost(navController, startDestination = Screen.Home.route, modifier) {
        composable(Screen.Home.route) {
            ConvertCurrency(
            )
        }

    }
}

    @Composable
    fun navigationTitle(navController: NavController): String {
        return when (currentRoute(navController)) {
            Screen.MovieDetail.route -> stringResource(id = R.string.app_name)
//            Screen.Login.route -> stringResource(id = R.string.login)
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
