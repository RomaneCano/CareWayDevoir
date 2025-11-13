package com.example.myapplication.ihm

import androidx.compose.runtime.*
import com.example.myapplication.viewmodel.CareWayViewModel
import com.example.myapplication.model.AppScreen
import com.example.myapplication.ihm.screens.QrCodeScreen
import com.example.myapplication.ihm.screens.TripDetailsScreen
import com.example.myapplication.ihm.screens.MainScreenView

@Composable
fun CareWayApp(vm: CareWayViewModel) {
    val currentScreen by vm.currentScreen.collectAsState()
    val bottomIndex by vm.bottomIndex.collectAsState()
    val selectedTrip by vm.selectedTrip.collectAsState()

    when (currentScreen) {
        AppScreen.MainApplication -> MainScreenView(
            selectedTabIndex = bottomIndex,
            onTabSelected = vm::onBottomTabSelected,
            onQrCodeClicked = vm::openQr,
            onNavigateToTripDetails = vm::openTripDetails
        )
        AppScreen.QrCodeFlow -> QrCodeScreen(
            onBackClicked = { vm.backToMain() }
        )
        AppScreen.TripDetails -> {
            selectedTrip?.let { trip ->
                TripDetailsScreen(
                    tripInfo = trip,
                    onBack = vm::closeTripDetails,
                    bottomNavDisplayItems = listOf(
                        BottomNavItem.Home,
                        BottomNavItem.Search,
                        BottomNavItem.History,
                        BottomNavItem.Profile
                    ),
                    selectedBottomNavIndex = bottomIndex,
                    onBottomNavItemSelected = vm::onBottomTabSelected,
                    onQrCodeClicked = vm::openQr
                )
            } ?: vm.backToMain()
        }
    }
}
