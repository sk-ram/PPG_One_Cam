package com.example.PWV

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InputActivity : Activity() {
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input)
        val button : Button = findViewById(R.id.submit)
        val bp : TextView = findViewById(R.id.bp)
        val glucose : TextView = findViewById(R.id.glucose)
        button.setOnClickListener(View.OnClickListener {
            var user = Firebase.auth.currentUser?.uid
            if (!user.isNullOrEmpty()) {
                val ref = db.collection("users").document(user)

                ref
                    .update("bp", bp.text)
                    .addOnSuccessListener { Log.d("BP", "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w("BP", "Error updating document", e) }

                ref
                    .update("glucose", glucose.text)
                    .addOnSuccessListener { Log.d("GLUCOSE", "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w("GLUCOSE", "Error updating document", e) }
            }

            Log.d("bp", bp.text.toString())
            Log.d("glucose", glucose.text.toString())
        })
    }
}