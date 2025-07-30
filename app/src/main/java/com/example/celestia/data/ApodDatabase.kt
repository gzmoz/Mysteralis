package com.example.celestia.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ApodEntity::class], version = 2)  //Use the class named ApodEntity as a table
//ApodDatabase is the database class that Room will manage. DAO classes are accessed from here.
abstract class ApodDatabase : RoomDatabase() { //This class is a database class that Room will manage.

    abstract fun apodDao(): ApodDao //says: “I have data access rules in my database called ApodDao.”So you can access DAO functions like insert(), getAll().

    //companion object = Kotlin is the equivalent of static in Java.
    companion object{ // creates singleton db

        /* companion object: It is used to define static members of a class in Kotlin.
        So this is a must to be able to call ApodDatabase.getInstance(context) from somewhere else.*/
        private var INSTANCE: ApodDatabase? = null //INSTANCE: Defined to share the same database object across the application.

        fun getInstance(context: Context): ApodDatabase{ //This function allows you to access the database from anywhere in the application.
           /* return INSTANCE ?: Room.databaseBuilder( /*INSTANCE ?:If the database has not been created before, create a new one.*/
                context.applicationContext,
                ApodDatabase::class.java,
                "apod_database" //An SQLite file named apod_database is created inside the phone
            ).build().also{ INSTANCE = it} //save the created DB object in the INSTANCE variable so that it is not created again */

            return Room.databaseBuilder(
                context,
                ApodDatabase::class.java,
                "apod_database"
            )
                .fallbackToDestructiveMigration() // ❗Eski veritabanını siler ve yeniden oluşturur
                .build()


        }



    }





}

