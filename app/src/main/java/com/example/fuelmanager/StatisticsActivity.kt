package com.example.fuelmanager

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.fuelmanager.extensions.MyXAxisFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_statistics.*

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val totalKmValue: TextView = totalKmValue
        val totalFuelValue: TextView = totalFuelValue
        val totalFuelUpsValue: TextView = totalFuelUpsValue
        val totalSumValue: TextView = totalSumValue
        val averageConsumptionValue: TextView = averageConsumptionValue
        val graph1: LineChart = graph1
        val graph2: LineChart = graph2

        val mDatabase = FirebaseDatabase.getInstance().reference.child("fillups")

        mDatabase.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(datasnapshot: DataSnapshot) {
                var mKm = 0.0
                var mFuel = 0.0
                var mFuelUps = datasnapshot.childrenCount.toInt()
                var mSum = 0.0
                var mAverage = 0.0

                var map: Map< String,Any>

                //graph1 data
                var data: Entry
                var points: MutableList<Entry> = mutableListOf()
                var dateArray = arrayOfNulls<String>(mFuelUps)

                //graph2 data
                var data2: Entry
                var points2: MutableList<Entry> = mutableListOf()

                var i = 0
                var z = 0f
                var j = 0f

                for (ds in datasnapshot.children){
                    map = ds.value as Map<String, Any>
                    val traveledKm = map["traveledKm"]
                    val fuel = map["amountOfLiter"]
                    val sum = map["sum"]
                    val average = map["average"]
                    val date = map["date"]

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
                    mAverage = averageValue
                    averageConsumptionValue.text = String.format("%.2f", mAverage) + " l/100 Km"

                    totalFuelUpsValue.text = mFuelUps.toString()

                    val dateValue = date.toString()

                    dateArray[i++] = dateValue

                    //graph1 data
                    data = Entry(j++, sumValue.toFloat())
                    points.add(data)

                    //graph2 data
                    data2 = Entry(z++, averageValue.toFloat())
                    points2.add(data2)

                }

                //graph1 formatting
                val lds = LineDataSet(points, "Összeg")
                lds.axisDependency = YAxis.AxisDependency.LEFT
                lds.color = Color.rgb(26, 132, 60)
                lds.setCircleColor(Color.rgb(26, 132, 60))
                lds.lineWidth = 2f

                val dataSet: MutableList<ILineDataSet> = mutableListOf()
                dataSet.add(lds)

                val datas = LineData(dataSet)

                graph1.data = datas
                graph1.invalidate()
                graph1.legend.isEnabled = false
                graph1.axisRight.isEnabled = false
                graph1.minimumHeight = 250
                graph1.extraBottomOffset = 50f
                graph1.description.isEnabled = false
                graph1.setVisibleXRangeMaximum(10f)

                val formatter = MyXAxisFormatter(dateArray)

                val xAxis = graph1.xAxis
                xAxis.granularity = 1f
                xAxis.valueFormatter = formatter
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawAxisLine(false)
                xAxis.labelRotationAngle = -90f


                //graph2 formatting
                val lds2 = LineDataSet(points2, "Átlagfogyasztás")
                lds2.axisDependency = YAxis.AxisDependency.LEFT
                lds2.color = Color.rgb(26, 132, 60)
                lds2.setCircleColor(Color.rgb(26, 132, 60))
                lds2.lineWidth = 2f

                val dataSet2: MutableList<ILineDataSet> = mutableListOf()
                dataSet2.add(lds2)

                val datas2 = LineData(dataSet2)

                graph2.data = datas2
                graph2.invalidate()
                graph2.legend.isEnabled = false
                graph2.axisRight.isEnabled = false
                graph2.minimumHeight = 250
                graph2.extraBottomOffset = 50f
                graph2.description.isEnabled = false
                graph2.setVisibleXRangeMaximum(10f)

                val xAxis2 = graph2.xAxis
                xAxis2.granularity = 1f
                xAxis2.valueFormatter = formatter
                xAxis2.position = XAxis.XAxisPosition.BOTTOM
                xAxis2.setDrawAxisLine(false)
                xAxis2.labelRotationAngle = -90f

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    //fun String.toDate(format: String): Date = SimpleDateFormat(format, Locale.getDefault()).parse(this)
}
