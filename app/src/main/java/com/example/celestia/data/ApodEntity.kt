package com.example.celestia.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//Identifies the table in the database

@Entity(tableName = "apod_table")
data class ApodEntity(
    @PrimaryKey val date: String,
    val title: String,
    val explanation: String,
    val url: String,
    val isFavorite: Boolean = false
)
