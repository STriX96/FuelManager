package com.example.fuelmanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_statistics.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val totalKmValue: TextView = totalKmValue
        val totalFuelValue: TextView = totalFuelValue
        val totalFuelUpsValue: TextView = totalFuelUpsValue
        val totalSumValue: TextView = totalSumValue
        val averageConsumptionValue: TextView = averageConsumptionValue
        //val graph1: GraphView = graph1
        //var series: LineGraphSeries<DataPoint>

        val mDatabase = FirebaseDatabase.getInstance().reference.child("fillups")

        mDatabase.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(datasnapshot: DataSnapshot) {
                var mKm = 0.0
                var mFuel = 0.0
                var mFuelUps = 0
                var mSum = 0.0
                var mAverage = 0.0

                var map: Map< String,Any>

                for (ds in datasnapshot.children){
                    map = ds.value as Map<String, Any>
                    val traveledKm = map["traveledKm"]
                    val fuel = map["amountOfLiter"]
                    val sum = map["sum"]
                    val average = map["average"]
                    //val date = map["date"]

                    val traveledKmValue = traveledKm.toString().toDouble()
                    mKm += traveledKmValue
                    totalKmValue.text = String.format("%.1f", mKm) + " Km"

                    val fuelValue = fuel.toString().toDouble()
                    mFuel += fuelValue
                    totalFuelValue.text = String.format("%.2f", mFuel) + " l"

                    val sumValue = sum.toString().toDouble()
                    mSum += sumValue
                    totalSumValue.text = String.format("%.0f", mSum) + " Ft"

                    val averageValue = average.toString().toDouble()
                    mAverage += averageValue
                    averageConsumptionValue.text = String.format("%.2f", mAverage) + " l/100 Km"

                    mFuelUps++
                    totalFuelUpsValue.text = mFuelUps.toString()

                }

                //val dateValue = date.toString()
                //val formatter: DateFormat = SimpleDateFormat("YYYY.MM.DD", Locale.UK)
                //val dateToDate : Date = formatter.parse(dateValue)

                //graph1
                //points = arrayOfNulls<DataPoint>(mFuelUps)
                //val data = DataPoint(dateToDate, sumValue)
                //points[mFuelUps-1] = data

                //series = LineGraphSeries()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
}
