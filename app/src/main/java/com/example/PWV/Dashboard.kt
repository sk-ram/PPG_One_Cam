package com.example.PWV

import android.app.Activity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log

class Dashboard : Activity() {
    val db = Firebase.firestore
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
    }

    private fun getData() {
        db.collection("users").get().addOnSuccessListener {
            result ->
            for (document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")
            }
        }
            .addOnFailureListener {
                exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


}