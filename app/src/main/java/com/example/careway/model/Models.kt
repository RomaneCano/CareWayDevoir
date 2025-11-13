package com.example.careway.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class UpcomingTripInfo(
    val id: String,
    val driverName: String,
    val transportDetails: String,
    val date: String,
    val time: String,
    val routeDetails: String,
    val isGroup: Boolean = false,
    val profilePicVector: ImageVector = Icons.Filled.AccountCircle,
    val profileImagePainter: Painter? = null
)

data class CompletedTripInfo(
    val id: String,
    val driverName: String,
    val transportDetails: String,
    val date: String,
    val time: String,
    val profilePic: ImageVector = Icons.Filled.AccountCircle
)

data class PendingRequestInfo(
    val id: String,
    val transporterName: String,
    val transporterPhone: String,
    val date: String,
    val time: String,
    val requestAge: String,
    val profilePic: ImageVector = Icons.Filled.Domain
)

data class ActionButtonData(val icon: ImageVector, val label: String)

data class TransporterInfo(
    val id: String,
    val name: String,
    val phone: String,
    val distance: String,
    val rating: String,
    val reviews: String,
    val availability: String,
    val imageRes: Int? = null
)

enum class AppScreen {
    MainApplication,
    QrCodeFlow,
    TripDetails
}
