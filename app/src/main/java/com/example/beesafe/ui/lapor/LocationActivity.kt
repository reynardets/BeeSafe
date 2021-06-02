package com.example.beesafe.ui.lapor

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.beesafe.databinding.ActivityLocationBinding
import com.example.beesafe.utils.SharedPref
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class LocationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLocationBinding
    private lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var pref: SharedPref
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude = 0.0
    private var longitude = 0.0

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SharedPref(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mMapView = binding.mapView2
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()
        try {
            MapsInitializer.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView.getMapAsync { mMap ->
            googleMap = mMap
            // For showing a move to my location button
            googleMap.setMyLocationEnabled(true)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val position = LatLng(location.latitude, location.longitude)
                // For zooming automatically to the location of the marker
                val cameraPosition = CameraPosition.Builder().target(position).zoom(15f).build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
            googleMap.setOnMapClickListener {
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(LatLng(it.latitude,it.longitude)).title("Tempat Kejadian"))
                latitude = it.latitude
                longitude = it.longitude
            }
        }

        binding.btnSetlocation.setOnClickListener {
            val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address>
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addresses[0].getAddressLine(0)
            pref.setAddress(address)
            pref.setLocation(latitude.toFloat(),longitude.toFloat())
            onBackPressed()
        }
    }
}