package com.skyline_infosystem.basiccalculator.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.getTrimmedText(): String {
    return this.text.toString().trim()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.setSafeClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(defaultInterval = 100) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun addDelay(timeInMillis: Long, runBlock: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(Runnable(runBlock), timeInMillis)
}