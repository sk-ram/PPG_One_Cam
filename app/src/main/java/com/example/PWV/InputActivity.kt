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
import io.realm.kotlin.Realm
import io.realm.kotlin.internal.platform.runBlocking
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.types.RealmObject

class InputActivity : Activity() {
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input)
        val button : Button = findViewById(R.id.submit)
        val bpText : TextView = findViewById(R.id.bp)
        val glucoseText : TextView = findViewById(R.id.glucose)
        button.setOnClickListener(View.OnClickListener {
            val app = App.create("cardios-data-uyrcp")
// use constants for query names so you can edit or remove them later
            val NAME_QUERY = "NAME_QUERY"
            runBlocking {
                val user = app.currentUser
                if (user?.loggedIn == true) {
                    val config = SyncConfiguration.Builder(user, setOf(BioMarker::class)).build()
                    val realm = Realm.open(config)
                    Log.d("REALM","Successfully opened realm: ${realm.configuration.name}")

                    realm.writeBlocking {
                        copyToRealm(BioMarker().apply {
                            id = user.identity
                            bp = bpText.text.toString()
                            glucose = glucoseText.text.toString()
                        })
                    }
                }
            }

            Log.d("bp", bpText.text.toString())
            Log.d("glucose", glucoseText.text.toString())
        })
    }
}

class BioMarker : RealmObject {
    lateinit var id: String
    lateinit var bp: String
    lateinit var glucose: String
}