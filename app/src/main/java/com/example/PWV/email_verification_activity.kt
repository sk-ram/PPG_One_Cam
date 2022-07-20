package com.example.PWV

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class email_verification_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_verification)

        var contineToMainActivity= findViewById<Button>(R.id.button3)
        contineToMainActivity.setOnClickListener {
            val intent = Intent(this, dashboard_activity::class.java)
            startActivity(intent)
        }
    }
}