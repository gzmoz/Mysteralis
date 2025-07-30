package com.example.celestia.data

import androidx.room.Dao
import androidx.room.*
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query

//Defines how access data -dao: database communication class

@Dao // Data Access Object
interface ApodDao {

    //Adds a new photo data to the database. Since the date is already the primary key, it updates if the same day exists.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apod: ApodEntity)

    @Query("SELECT * FROM apod_table ORDER BY date DESC")
    fun getAll(): Flow<List<ApodEntity>>

    //to calendar function
    @Query("SELECT * FROM apod_table WHERE date = :date")
    fun getApodByDate(date: String): Flow<List<ApodEntity>>

    @Query("UPDATE apod_table SET isFavorite = :isFavorite WHERE date = :date")
    suspend fun updateFavoriteStatus(date: String, isFavorite: Boolean)

    @Query("SELECT * FROM apod_table WHERE isFavorite = 1")
    fun getFavoriteApods(): Flow<List<ApodEntity>>

}

/*Flow is the structure of Kotlin that provides asynchronous data flow.
In other words: “A system that automatically notifies and updates as data changes.”*/
//I used Flow because: I want the Application interface to be automatically updated as the user adds data to the database.

/*Note:Flow only flows data, works asynchronously
To collect data, functions such as collect, collectAsState are used*/