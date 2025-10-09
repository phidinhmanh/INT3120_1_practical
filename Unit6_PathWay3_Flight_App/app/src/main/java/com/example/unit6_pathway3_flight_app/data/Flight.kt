package com.example.unit6_pathway3_flight_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val iataCode: String,
    val name: String,
    val passengers: Int
)

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val departureCode: String,
    val destinationCode: String
)


