package com.example.careway.ihm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("Accueil", Icons.Filled.Home)
    object Search : BottomNavItem("Recherche", Icons.Filled.Search)
    object History : BottomNavItem("Historique", Icons.Filled.DateRange)
    object Profile : BottomNavItem("Profil", Icons.Filled.Person)
}
