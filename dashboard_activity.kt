package com.example.PWV


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.firebase.firestore.FirebaseFirestore


class dashboard_activity : AppCompatActivity() {









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        var toMainActivityFromDashboard = findViewById<Button>(R.id.button6)
        toMainActivityFromDashboard.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val db=FirebaseFirestore.getInstance()
        var chart=findViewById<PieChart>(R.id.pieChartGraph)

        db.collection("users")
            .get()
            .addOnCompleteListener{
                val result:StringBuffer=StringBuffer()

                if(it.isSuccessful){
                    for(document in it.result!!) {
                        result.append(document.data.getValue("latestTestResults")).append(" ")
                            .append("\n\n")
                    }

                    var values=result.split(" ")

                    var labels=ArrayList<String>()
                    labels.add("Arrythmia")
                    labels.add("Congestive Heart Failure")
                    labels.add("Coronary Artery Disease")
                    labels.add("High Blood Pressure")
                    labels.add("Peripheral Artery Disease")


                    var arrythmiaScore=values[0]

                    arrythmiaScore=arrythmiaScore.substring(arrythmiaScore.indexOf("=")+1, arrythmiaScore.indexOf(","))


                    var congestiveHeartFaliureScore=values[1]
                    congestiveHeartFaliureScore=congestiveHeartFaliureScore.substring(congestiveHeartFaliureScore.indexOf("=")+1, congestiveHeartFaliureScore.indexOf(","))


                    var coronaryArteryDiseaseScore=values[2]

                    coronaryArteryDiseaseScore=coronaryArteryDiseaseScore.substring(coronaryArteryDiseaseScore.indexOf("=")+1, coronaryArteryDiseaseScore.indexOf(","))


                    var highBloodPressureScore=values[3]

                    highBloodPressureScore=highBloodPressureScore.substring(highBloodPressureScore.indexOf("=")+1, highBloodPressureScore.indexOf(","))


                    var peripheralArteryDiseaseScore=values[4]

                    peripheralArteryDiseaseScore=peripheralArteryDiseaseScore.substring(peripheralArteryDiseaseScore.indexOf("=")+1, peripheralArteryDiseaseScore.indexOf(","))

                    val pieChartEntries: MutableList<PieEntry> = ArrayList()
                    pieChartEntries.add( PieEntry(14f,"Arrythmia") )
                    pieChartEntries.add( PieEntry(15f,"Congestive Heart Failure") )
                    pieChartEntries.add( PieEntry(16f,"Coronary Artery Disease") )
                    pieChartEntries.add( PieEntry(17f,"High Blood Pressure") )
                    pieChartEntries.add( PieEntry(18f,"Peripheral Artery Disease") )





                    chart.setUsePercentValues(true);
                    chart.getDescription().setEnabled(false);
                    chart.getLegend().setEnabled(false);

                    chart.setDragDecelerationFrictionCoef(0.95f);
                    chart.setDrawHoleEnabled(true);
                    chart.setHoleColor(Color.WHITE);
                    chart.setTransparentCircleColor(Color.WHITE);
                    chart.setTransparentCircleAlpha(110);
                    chart.setHoleRadius(58f);
                    chart.setTransparentCircleRadius(61f);
                    chart.setDrawCenterText(true);

                    chart.setRotationEnabled(true);
                    chart.setHighlightPerTapEnabled(true);

                    chart.setEntryLabelColor(Color.WHITE);
                    chart.setEntryLabelTextSize(12f);
                    val piedataset=PieDataSet(pieChartEntries,"Diagnosis")
                    piedataset.setDrawIcons(false)
                    piedataset.setSliceSpace(3f)

                    piedataset.setSelectionShift(5f)
                    val data=PieData(piedataset)
                    data.setDrawValues(true)
                    data.setValueFormatter(PercentFormatter(chart))
                    data.setValueTextSize(12f)
                    data.setValueTextColor(Color.BLACK)



                    chart.data=data
                    chart.highlightValues(null)
                    chart.invalidate()




    }

            }

    }
}