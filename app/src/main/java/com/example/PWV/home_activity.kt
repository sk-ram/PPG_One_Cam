package com.example.PWV

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class home_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        var registerUserBtn = findViewById<Button>(R.id.button)
        var loginUserBtn = findViewById<Button>(R.id.button2)
        registerUserBtn.setOnClickListener {
            val intent = Intent(this, register_activity::class.java)
            startActivity(intent)
        }
        loginUserBtn.setOnClickListener {
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }

    }
}