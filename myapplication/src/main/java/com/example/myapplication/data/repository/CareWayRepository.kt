package com.example.careway.data.repository

import com.example.careway.model.*
import com.example.careway.data.remote.RemoteDataSource
import com.example.myapplication.data.local.LocalDataSource

interface CareWayRepository {
    suspend fun getUpcomingTrips(): List<UpcomingTripInfo>
    suspend fun getCompletedTrips(): List<CompletedTripInfo>
    suspend fun getPendingRequests(): List<PendingRequestInfo>
    suspend fun getNearbyTransporters(): List<TransporterInfo>
    suspend fun searchTransporters(query: String): List<TransporterInfo>
}

class CareWayRepositoryImpl(
    private val local: LocalDataSource = LocalDataSource(),
    private val remote: RemoteDataSource = RemoteDataSource()
): CareWayRepository {

    override suspend fun getUpcomingTrips() = remote.getUpcomingTrips()
    override suspend fun getCompletedTrips() = remote.getCompletedTrips()
    override suspend fun getPendingRequests() = remote.getPendingRequests()
    override suspend fun getNearbyTransporters() = remote.getNearbyTransporters()
    override suspend fun searchTransporters(query: String) = remote.searchTransporters(query)
}
