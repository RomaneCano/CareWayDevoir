package com.example.careway.ihm

import androidx.compose.runtime.*
import com.example.careway.viewmodel.CareWayViewModel
import com.example.careway.model.*

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
                    bottomNavDisplayItems = listOf(BottomNavItem.Home, BottomNavItem.Search, BottomNavItem.History, BottomNavItem.Profile),
                    selectedBottomNavIndex = bottomIndex,
                    onBottomNavItemSelected = vm::onBottomTabSelected,
                    onQrCodeClicked = vm::openQr
                )
            } ?: vm.backToMain()
        }
    }
}
