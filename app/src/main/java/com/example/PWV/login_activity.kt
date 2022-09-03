package com.example.PWV

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.realm.kotlin.internal.platform.runBlocking
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials

class login_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val login = findViewById<Button>(R.id.login)
        val loginEmail = findViewById<EditText>(R.id.loginEmail)
        val loginPassword = findViewById<EditText>(R.id.loginPassword)
        val firebaseEmail = loginEmail.text.toString()
        val firebasePassword = loginPassword.text.toString()
        login.setOnClickListener{
            signIn(firebaseEmail, firebasePassword)
            val intent = Intent(this, dashboard_activity::class.java)
            startActivity(intent)
        }

        var toRegister = findViewById<TextView>(R.id.textView5)
        toRegister.setOnClickListener {
            val intent = Intent(this, register_activity::class.java)
            startActivity(intent)
        }
    }
    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        val app: App = App.create("cardios-data-uyrcp") // Replace this with your App ID
        runBlocking { // use runBlocking sparingly -- it can delay UI interactions
            val user = app.login(Credentials.emailPassword(email, password))
        }
        // [END sign_in_with_email]
    }

    companion object {
        const val TAG = "Login"
    }
}