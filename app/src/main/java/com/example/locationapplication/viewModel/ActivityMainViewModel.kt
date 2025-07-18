package com.example.locationapplication.viewModel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.locationapplication.model.repository.FusedLocationRepository

class ActivityMainViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository = FusedLocationRepository(application.applicationContext)

    private val getLastKnownLocation = MutableLiveData<Location?>()
    val lvLastKnownLocation: LiveData<Location?> = getLastKnownLocation

    private val getCurrentLocation = MutableLiveData<Location?>()
    val lvCurrentLocation: LiveData<Location?> = getCurrentLocation

    fun fetchLastLocation() {
        locationRepository.getLastKnownLocation { location ->
            getLastKnownLocation.postValue(location)
        }
    }

    fun fetchCurrentLocation() {
        locationRepository.getCurrentLocation { location ->
            getCurrentLocation.postValue(location)
        }
    }
}
