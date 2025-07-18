package com.example.locationapplication.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.locationapplication.databinding.ActivityMainBinding
import com.example.locationapplication.model.util.LocationPermission
import com.example.locationapplication.model.util.NetworkUtils
import com.example.locationapplication.viewModel.ActivityMainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var activityMainViewModel: ActivityMainViewModel
    private lateinit var networkUtils: NetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        activityMainViewModel = ViewModelProvider(this)[ActivityMainViewModel::class.java]

        activityMainViewModel.lvLastKnownLocation.observe(this) {
            activityMainBinding.tvGetLastLocationValue.text =
                it?.let { "Lat: ${it.latitude}, Long: ${it.longitude}" }
        }
        activityMainViewModel.lvCurrentLocation.observe(this) {
            activityMainBinding.tvGetCurrentLocationValue.text =
                it?.let { "Lat: ${it.latitude}, Long: ${it.longitude}" }
        }

        networkUtils = NetworkUtils()
        activityMainBinding.buttonGetLastKnownLocation.setOnClickListener {
            if (LocationPermission.hasLocationPermission(this)) {
                if (networkUtils.isNetworkAvailable(this)) {
                    activityMainViewModel.fetchLastLocation()
                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            } else {
                LocationPermission.requestLocationPermission(this)
            }
        }

        activityMainBinding.buttonClickGetCurrentLocation.setOnClickListener {
            if (LocationPermission.hasLocationPermission(this)) {
                if (networkUtils.isNetworkAvailable(this)) {
                    activityMainViewModel.fetchCurrentLocation()
                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            } else {
                LocationPermission.requestLocationPermission(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationPermission.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
                activityMainViewModel.fetchLastLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}