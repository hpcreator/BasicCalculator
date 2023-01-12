package com.skyline_infosystem.basiccalculator.utils

import android.os.SystemClock
import android.view.View

class SafeClickListener(
    var defaultInterval: Int = 1000, // to add delay of 1 sec
    private val onSafeCLick: (View) -> Unit,
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}