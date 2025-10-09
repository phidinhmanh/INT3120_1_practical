package com.example.unit6_pathway3_flight_app.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Database(entities = [Airport::class, Favorite::class], version = 1)
abstract class FlightDataBase: RoomDatabase() {
    abstract fun flightDao(): FlightDao

    companion object {
        @Volatile
        private var INSTANCE: FlightDataBase? = null

        fun getDatabase(context: Context): FlightDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlightDataBase::class.java,
                    "flight_database"
                ).build().also { INSTANCE = it }
                instance
            }
        }
    }
}

@Dao
interface FlightDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getFavoriteById(id: Int): Flow<Favorite?>

    @Query("SELECT * FROM airport WHERE iataCode = :iataCode")
    fun getAirportByIataCode(iataCode: String): Flow<Airport?>

    @Query("SELECT * FROM favorite WHERE departureCode = :departureCode")
    fun getFavoriteByDeparture(departureCode: String): Flow<List<Favorite>>

    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirport(airport: Airport)

    @Update
    suspend fun updateFavorite(favorite: Favorite)

    @Query("SELECT * FROM airport")
    fun getAllAirports(): Flow<List<Airport>>



}