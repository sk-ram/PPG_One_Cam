package com.example.PWV

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class thank_you_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thank_you)
        var toDashboardFromthank = findViewById<Button>(R.id.button5)
        toDashboardFromthank.setOnClickListener{
            val intent = Intent(this, dashboard_activity::class.java)
            startActivity(intent)
        }



    }


}