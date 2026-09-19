package com.uniruta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uniruta.app.ui.navigation.UniRutaApp
import com.uniruta.app.ui.theme.UniRutaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniRutaTheme {
                UniRutaApp()
            }
        }
    }
}
