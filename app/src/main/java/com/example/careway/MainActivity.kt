package com.example.careway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.careway.ui.theme.CareWayTheme
import com.example.careway.ihm.BottomNavItem
import com.example.careway.ihm.*
import com.example.careway.viewmodel.CareWayViewModel
import com.example.careway.model.*

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
