package com.example.PWV

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class register_activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        auth = Firebase.auth

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
                sendEmailVerification()
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
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload()
        }
    }

    private fun reload() {

    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
        // [END send_email_verification]
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        // [END create_user_with_email]
    }

    companion object {
        const val TAG = "Register"
    }
}