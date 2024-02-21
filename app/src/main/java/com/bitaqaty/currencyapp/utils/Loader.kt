package com.bitaqaty.currencyapp.utils


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bitaqaty.currencyapp.R
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle


@SuppressLint("StaticFieldLeak")
lateinit var loadingContainer: LinearLayout
var isloading = false
fun Fragment.loadingW() {


    lateinit var imageView: ImageView
    val pro: SpinKitView
    val doubleBounce: Sprite = Circle()

    if (!isloading) {
        imageView = ImageView(this.requireContext())
        isloading = true
        pro = SpinKitView(this.requireContext())
        pro.setColor(ActivityCompat.getColor(this.requireContext(), R.color.white))
        pro.setIndeterminateDrawable(doubleBounce)
        val params = ConstraintLayout.LayoutParams(
            320,
            320
        )
        params.bottomMargin = 270

//        Glide.with(fragment).asGif()
//            .load(R.raw.tasweyah_loader_ar).into(imageView)
        imageView.layoutParams = params
        imageView.background =
            ActivityCompat.getDrawable(this.requireContext(), R.mipmap.ic_launcher_round)
        loadingContainer = LinearLayout(this.requireContext())

        val pra = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )


        loadingContainer.layoutParams = pra
        loadingContainer.setBackgroundColor(Color.parseColor("#00FFFFFF"))
        loadingContainer.addView(imageView)
        loadingContainer.gravity = Gravity.CENTER


        (this.requireActivity().window.decorView as ViewGroup).addView(loadingContainer)

        this.requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        pro.isVisible = true
    } else {
        this.hideLoadingW()
    }
}

fun Fragment.hideLoadingW() {
    if (isloading) {
        isloading = false
        (this.requireActivity().window.decorView.rootView as ViewGroup).removeView(loadingContainer)
        this.requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}

