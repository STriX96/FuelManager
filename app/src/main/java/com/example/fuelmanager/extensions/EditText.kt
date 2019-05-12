package com.example.fuelmanager.extensions

import android.widget.EditText
import com.example.fuelmanager.R

fun EditText.validateNonEmpty(): Boolean {
    if (text.isEmpty()) {
        error = context.getString(R.string.validate_required)
        return false
    }
    return true
}