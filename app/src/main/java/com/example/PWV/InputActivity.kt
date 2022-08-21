package com.example.PWV

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class InputActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input)
        val button : Button = findViewById(R.id.submit)
        val bp : TextView = findViewById(R.id.bp)
        val glucose : TextView = findViewById(R.id.glucose)
        button.setOnClickListener(View.OnClickListener {
            Log.d("bp", bp.text.toString())
            Log.d("glucose", glucose.text.toString())
        })
    }
}