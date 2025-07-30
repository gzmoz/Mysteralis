package com.example.celestia.repository

import com.example.celestia.data.ApodDao
import com.example.celestia.data.ApodEntity
import com.example.celestia.model.ApodDataModel
import com.example.celestia.network.ApodApiService
import kotlinx.coroutines.flow.Flow

/*This class is the Repository layer of the MVVM architecture and
 bridges the gap between the data sources (API and Room DB) and the ViewModel.*/
//repository:will both pull data from Retrofit and write to Room
class ApodRepository(
    private val dao: ApodDao,
    private val api: ApodApiService
) {
    fun getAllApods(): Flow<List<ApodEntity>> = dao.getAll()
    fun getApodByDate(date: String): Flow<List<ApodEntity>> = dao.getApodByDate(date)

    //Gets data from API → Writes to Room
    suspend fun fetchAndSaveApod(date:String){
        val response: ApodDataModel = api.getApod(date=date)
        val entity = ApodEntity(
            date = response.date,
            title = response.title,
            explanation = response.explanation,
            url = response.url
        )

        dao.insert(entity)
    }

    suspend fun fetchApodByDate(date: String): ApodDataModel {
        return api.getApod(date = date)
    }
    suspend fun setFavoriteStatus(date: String, isFavorite: Boolean) {
        dao.updateFavoriteStatus(date, isFavorite)
    }

    fun getFavorites(): Flow<List<ApodEntity>> = dao.getFavoriteApods()


}

/*dao.getAll() Gets all data from Room (as Flow)
api.getApod() Gets a photo of a specific date from API
dao.insert(...) Writes this data to Room database*/