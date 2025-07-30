package com.example.celestia.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celestia.data.ApodEntity
import com.example.celestia.model.ApodDataModel
import com.example.celestia.repository.ApodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//The ViewModel continues to live even if the UI is redrawn
class ApodViewModel(private val repository: ApodRepository) : ViewModel() {

    private val _selectedApod = MutableStateFlow<ApodDataModel?>(null)
    val selectedApod: StateFlow<ApodDataModel?> = _selectedApod

    private val _dataNotFound = MutableStateFlow(false)
    val dataNotFound: StateFlow<Boolean> = _dataNotFound

    //apods:Gives data from Room to UI via Flow
    val apods = repository.getAllApods()  //  Flow<List<ApodEntity>> that comes from Room Dao
        .map { it.sortedByDescending { it.date } } //sort each time data comes in → according to the date field, the newest is at the top
        .stateIn(
            viewModelScope,  //lifecycle for coroutine
            SharingStarted.WhileSubscribed(), //listen while Ui is active
            emptyList()
        )

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchToday(){ //Retrieves data from API and writes to DB
        val today = java.time.LocalDate.now().toString()
        viewModelScope.launch { //start coroutine
            try {
                Log.d("CelestiaDebug", "Fetching data for $today")
                repository.fetchAndSaveApod(today)
            } catch (e: Exception) {
                Log.e("CelestiaDebug", "API call failed: ${e.message}")
            }
        }
    }

    fun fetchByDate(date: String) {
        /*viewModelScope.launch {
            _selectedApod.value = repository.fetchApodByDate(date)
        }*/
        viewModelScope.launch {
            try {
                val result = repository.fetchApodByDate(date)
                _selectedApod.value = result
                _dataNotFound.value = false
            } catch (e: Exception) {
                Log.e("CelestiaDebug", "APOD not found for $date: ${e.message}")
                _selectedApod.value = null
                _dataNotFound.value = false  // önce false yap
                _dataNotFound.value = true   // sonra tekrar true → tetiklenir
            }
        }
    }

    fun resetDataNotFound() {
        _dataNotFound.value = false
    }


    fun clearSelectedApod() {
        _selectedApod.value = null
    }

    fun apodByDate(date: String): Flow<List<ApodEntity>> {
        return repository.getApodByDate(date)
    }

    fun toggleFavorite(date: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.setFavoriteStatus(date, !currentStatus)
        }
    }

    val favoritePhotos: StateFlow<List<ApodEntity>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


}