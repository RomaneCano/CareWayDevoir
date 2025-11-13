package com.example.careway.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.careway.model.*
import com.example.careway.data.repository.CareWayRepository
import com.example.careway.data.repository.CareWayRepositoryImpl

class CareWayViewModel(
    private val repository: CareWayRepository = CareWayRepositoryImpl()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.MainApplication)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _bottomIndex = MutableStateFlow(0)
    val bottomIndex: StateFlow<Int> = _bottomIndex.asStateFlow()

    private val _selectedTrip = MutableStateFlow<UpcomingTripInfo?>(null)
    val selectedTrip: StateFlow<UpcomingTripInfo?> = _selectedTrip.asStateFlow()

    private val _upcoming = MutableStateFlow<List<UpcomingTripInfo>>(emptyList())
    val upcoming: StateFlow<List<UpcomingTripInfo>> = _upcoming.asStateFlow()

    private val _completed = MutableStateFlow<List<CompletedTripInfo>>(emptyList())
    val completed: StateFlow<List<CompletedTripInfo>> = _completed.asStateFlow()

    private val _pending = MutableStateFlow<List<PendingRequestInfo>>(emptyList())
    val pending: StateFlow<List<PendingRequestInfo>> = _pending.asStateFlow()

    private val _nearby = MutableStateFlow<List<TransporterInfo>>(emptyList())
    val nearby: StateFlow<List<TransporterInfo>> = _nearby.asStateFlow()

    init {
        refreshAll()
    }

    fun refreshAll() = viewModelScope.launch {
        _upcoming.value = repository.getUpcomingTrips()
        _completed.value = repository.getCompletedTrips()
        _pending.value = repository.getPendingRequests()
        _nearby.value = repository.getNearbyTransporters()
    }

    fun onBottomTabSelected(index: Int) { _bottomIndex.value = index }

    fun openQr() { _currentScreen.value = AppScreen.QrCodeFlow }
    fun backToMain() { _currentScreen.value = AppScreen.MainApplication }
    fun openTripDetails(trip: UpcomingTripInfo) {
        _selectedTrip.value = trip
        _currentScreen.value = AppScreen.TripDetails
        _bottomIndex.value = 2
    }
    fun closeTripDetails() {
        _selectedTrip.value = null
        backToMain()
        _bottomIndex.value = 2
    }

    fun searchTransporters(query: String) = viewModelScope.launch {
        _nearby.value = repository.searchTransporters(query)
    }
}
