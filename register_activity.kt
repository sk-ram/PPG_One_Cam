package com.example.PWV

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class register_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var toLogin = findViewById<TextView>(R.id.toLoginRoute)
        toLogin.setOnClickListener {
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
        var toEmailVerificationRoute = findViewById<Button>(R.id.button4)
        toEmailVerificationRoute.setOnClickListener {
            val intent = Intent(this, email_verification_activity::class.java)
            startActivity(intent)
        }


    }
}