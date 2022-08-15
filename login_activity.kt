package com.example.PWV

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class login_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var toRegister = findViewById<TextView>(R.id.textView5)
        toRegister.setOnClickListener {
            val intent = Intent(this, register_activity::class.java)
            startActivity(intent)
        }
        var toMainActivityFromLogin = findViewById<Button>(R.id.login)
        toMainActivityFromLogin.setOnClickListener {
            val intent = Intent(this, dashboard_activity::class.java)
            startActivity(intent)
        }




    }
}