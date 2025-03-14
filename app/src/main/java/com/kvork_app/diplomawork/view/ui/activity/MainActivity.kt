package com.kvork_app.diplomawork.view.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.kvork_app.diplomawork.view.ui.navigation.AppNavHost
import com.kvork_app.diplomawork.view.ui.theme.DiplomaWorkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = FirebaseFirestore.getInstance()
        db.collection("test").document("firstDoc")
            .set(mapOf("message" to "Hello firstDoc"))

        setContent {
            val navController = rememberNavController()
            DiplomaWorkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    it
                    AppNavHost(navController = navController)
                }
            }
        }
    }

}