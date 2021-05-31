package com.example.beesafe.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.beesafe.api.APIConfig
import com.example.beesafe.databinding.FragmentHomeBinding
import com.example.beesafe.model.remote.APIResponse
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(){

    private lateinit var binding : FragmentHomeBinding
    private lateinit var mMapView : MapView
    private lateinit var googleMap : GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showGMaps(savedInstanceState)
        getReports()
    }

    @SuppressLint("MissingPermission")
    private fun showGMaps(savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mMapView = binding.mapView
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()
        try{
            MapsInitializer.initialize(requireContext())
        }catch (e : Exception){
            e.printStackTrace()
        }
        mMapView.getMapAsync { mMap ->
            googleMap = mMap
            // For showing a move to my location button
            googleMap.setMyLocationEnabled(true)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val position = LatLng(location.latitude ,location.longitude)
                // For dropping a marker at a point on the Map
                googleMap.addMarker(MarkerOptions().position(position).title("Marker Title")
                                .snippet("Marker Description")
                )
                // For zooming automatically to the location of the marker
                val cameraPosition = CameraPosition.Builder().target(position).zoom(15f).build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }

    private fun getReports() {
        val client = APIConfig.getAPIService().getReports()
        client.enqueue(object : Callback<APIResponse> {
            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                val arrayResponse = response.body()
                if(arrayResponse != null){
                    for (reports in arrayResponse.data){
                        //give color based on category
                        var circleColor = Color.TRANSPARENT
                        when(reports.category){
                            "category 1" -> circleColor = Color.GREEN
                            "category 2" -> circleColor = Color.YELLOW
                            "category 3" -> circleColor = Color.RED
                        }
                        //Drawing Circle
                        val circleOptions = CircleOptions()
                                .center(LatLng(reports.location.latitude, reports.location.longitude))
                                .radius(100.0)
                                .fillColor(circleColor)
                        googleMap.addCircle(circleOptions)
                    }
                }
            }
        })
    }
}