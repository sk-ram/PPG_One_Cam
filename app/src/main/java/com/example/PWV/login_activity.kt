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

class login_activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        auth = Firebase.auth

        val login = findViewById<Button>(R.id.login)
        val loginEmail = findViewById<EditText>(R.id.loginEmail)
        val loginPassword = findViewById<EditText>(R.id.loginPassword)
        val firebaseEmail = loginEmail.text.toString()
        val firebasePassword = loginPassword.text.toString()
        login.setOnClickListener{
            signIn(firebaseEmail, firebasePassword)
        }

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
    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        // [END sign_in_with_email]
    }

    companion object {
        const val TAG = "Login"
    }
}