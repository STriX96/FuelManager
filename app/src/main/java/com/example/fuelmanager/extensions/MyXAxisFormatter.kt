package com.example.fuelmanager.extensions

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

class MyXAxisFormatter(dateArray: Array<String?>) : IAxisValueFormatter{

    private val dates = dateArray

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return dates.getOrNull(value.toInt()) ?: value.toString()
    }
}