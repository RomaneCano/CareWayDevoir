package com.example.myapplication.data.local

import androidx.room.*

@Entity(tableName = "transporter")
data class TransporterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val distance: String,
    val rating: String,
    val reviews: String,
    val availability: String
)

@Dao
interface TransporterDao {
    @Query("SELECT * FROM transporter")
    suspend fun getAll(): List<TransporterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<TransporterEntity>)
}

@Database(entities = [TransporterEntity::class], version = 1)
abstract class CareWayDb : RoomDatabase() {
    abstract fun transporterDao(): TransporterDao
}

class LocalDataSource {
    // Placeholder until you provide a Room instance via DI
}
