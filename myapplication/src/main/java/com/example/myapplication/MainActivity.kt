package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.myapplication.viewmodel.CareWayViewModel
import com.example.myapplication.ihm.CareWayApp
import com.example.myapplication.ui.theme.CareWayTheme

class MainActivity : ComponentActivity() {
    private val vm: CareWayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CareWayTheme {
                CareWayApp(vm)
            }
        }
    }
}
