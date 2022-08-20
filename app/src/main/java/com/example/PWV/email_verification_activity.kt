package com.example.PWV

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class email_verification_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_verification)
        val user = Firebase.auth.currentUser

        var contineToMainActivity= findViewById<Button>(R.id.button3)
        contineToMainActivity.setOnClickListener {
            val intent = Intent(this, dashboard_activity::class.java)
            if (user?.isEmailVerified == true) {
                startActivity(intent)
            }
        }
    }
}