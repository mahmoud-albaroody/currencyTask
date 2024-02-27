package com.bitaqaty.currencyapp.utils

import androidx.navigation.navOptions
import com.bitaqaty.currencyapp.R


val navOptions = navOptions {
    anim {
        enter = R.anim.pop_enter_amin
    }
}

val navOptions1 = navOptions {
    anim {
        enter = R.anim.out
    }
}
val navOptions2 = navOptions {
    anim {
        popEnter = 0
        popExit = 0
        enter = R.anim.lift
        exit = 0
    }
}
