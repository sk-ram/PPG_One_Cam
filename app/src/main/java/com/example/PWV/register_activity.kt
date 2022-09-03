package com.example.PWV

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.realm.kotlin.internal.platform.runBlocking
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials

class register_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val register = findViewById<Button>(R.id.button4)
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<EditText>(R.id.editTextTextPassword)
        val confirmPassword = findViewById<EditText>(R.id.editTextTextPersonName3)
        val finalEmail = email.text.toString()
        val finalPassword = password.text.toString()
        val finalConfirmPassword = confirmPassword.text.toString()
        // set on-click listener

        // your code to perform when the user clicks on the button
        if (finalConfirmPassword == finalPassword) {
            register.setOnClickListener {
                createAccount(finalEmail, finalPassword)
            }
        }

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

    public override fun onStart() {
        super.onStart()
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        val app: App = App.create("cardios-data-uyrcp")
        runBlocking {
            app.emailPasswordAuth.registerUser(email, password)
            // once registered, you can log in with the user credentials
            val user = app.login(Credentials.emailPassword(email, password))
            Log.d(TAG,"Successfully logged in ${user.identity}")
        }
        // [END create_user_with_email]
    }

    companion object {
        const val TAG = "Register"
    }
}