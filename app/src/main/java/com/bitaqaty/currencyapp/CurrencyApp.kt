package com.bitaqaty.currencyapp

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CurrencyApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    }
}