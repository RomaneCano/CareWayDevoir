package com.example.careway.data.remote

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.myapplication.model.CompletedTripInfo
import com.example.myapplication.model.PendingRequestInfo
import com.example.myapplication.model.TransporterInfo
import com.example.myapplication.model.UpcomingTripInfo

// Retrofit skeleton (you can wire it later)
interface CareWayApi {
    // @GET("transporters") suspend fun transporters(): List<TransporterInfo>
    // etc.
}

class RemoteDataSource {
    suspend fun getUpcomingTrips() = listOf(
        UpcomingTripInfo(
            "2",
            "Olivier C.",
            "Ambu81, VSL",
            "Lundi 23 juin 2025",
            "17:45 - 18:30",
            "Lieu A > Lieu B",
            isGroup = true
        ),
        UpcomingTripInfo(
            "3",
            "Anne L.",
            "UrgVSL, VSL",
            "Mardi 24 juin 2025",
            "09:15 - 09:45",
            "Lieu C > Lieu D",
            profilePicVector = Icons.Filled.Person
        ),
        UpcomingTripInfo(
            "1",
            "Jean D.",
            "Ambu81, VSL",
            "Lundi 23 juin 2025",
            "11:00 - 11:30",
            "6 Rue Barclay > Hôpital de Castres"
        )
    )

    suspend fun getCompletedTrips() = listOf(
        CompletedTripInfo(
            "1",
            "Hélène A.",
            "Taxi Care, taxi conventionné",
            "Lundi 9 juin 2025",
            "16:30 - 17:00"
        ),
        CompletedTripInfo(
            "2",
            "Hélène A.",
            "Taxi Care, taxi conventionné",
            "Mercredi 7 mai 2025",
            "08:30 - 09:00"
        )
    )

    suspend fun getPendingRequests() = listOf(
        PendingRequestInfo(
            "1",
            "UrgVSL",
            "05 56 00 01 02",
            "Lundi 30 juin 2025",
            "10:15",
            "Depuis 2h",
            profilePic = Icons.Filled.AccountCircle
        ),
        PendingRequestInfo(
            "2",
            "Taxi Care",
            "06 01 02 03 00",
            "Jeudi 17 juillet 2025",
            "16:45 - 17:20",
            "Depuis 4j"
        )
    )

    suspend fun getNearbyTransporters() = listOf(
        TransporterInfo(
            "1",
            "UrgVSL",
            "05 56 00 01 02",
            "1.2 KM",
            "4,8",
            "120",
            "Ouvert de 8h à 19h"
        ),
        TransporterInfo("2", "Ambu81", "05 63 00 01 02", "2.0 KM", "4,6", "89", "Ouvert 24/7")
    )

    suspend fun searchTransporters(query: String) =
        getNearbyTransporters().filter { it.name.contains(query, ignoreCase = true) }
}
