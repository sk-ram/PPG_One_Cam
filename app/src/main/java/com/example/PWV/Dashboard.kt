package com.example.PWV


import android.app.Activity
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Dashboard : Activity() {
    private val db = Firebase.firestore
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        getData()
//        pieChart = findViewById(R.id.activity_main_piechart)

//        initPieChart()

    }

    val user = Firebase.auth.currentUser?.uid

    private fun getData() {
        db.collection("users").whereEqualTo("docID", user).get().addOnSuccessListener {
            result ->
            for (document in result) {
                val data = document.data
//                setDataToPieChart(data)
            }
        }
            .addOnFailureListener {
                exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

//    private lateinit var pieChart: PieChart


    /*private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        //adding padding
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.isWordWrapEnabled = true

    }*/


    // TODO: Figure out how to make a pieChart
    /*private fun setDataToPieChart(firebaseData) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(firebaseData.getValue(firebaseData.keys.elementAt(0)), firebaseData.latest[0].name))
        dataEntries.add(PieEntry(firebaseData.latest[1].percentage, firebaseData.latest[1].name))
        dataEntries.add(PieEntry(firebaseData.latest[2].percentage, firebaseData.latest[2].percentage))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#4DD0E1"))
        colors.add(Color.parseColor("#FFF176"))
        colors.add(Color.parseColor("#FF8A65"))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(15f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        //create hole in center
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = false





        pieChart.invalidate()

    }*/




}