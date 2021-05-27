package com.example.beesafe.ui.lapor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.beesafe.Model.Reports
import com.example.beesafe.R
import com.example.beesafe.api.APIConfig
import com.example.beesafe.databinding.FragmentLaporBinding
import com.example.beesafe.utils.SharedPref
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LaporFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLaporBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var pref: SharedPref
    private lateinit var mAuth : FirebaseAuth
    private lateinit var laporViewModel: LaporViewModel
    private var latitude = ""
    private var longitude = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaporBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
        getLatLong()
        binding.btnLapor.setOnClickListener(this)
    }

    private fun config() {
        pref = SharedPref(requireContext())
        mAuth = FirebaseAuth.getInstance()
        laporViewModel = LaporViewModel()
    }

    private fun getLatLong() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude.toString()
                longitude = location.longitude.toString()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_lapor ->{
                //Temporary Category
                val category = ""

                //get Current Date
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
                val currentDate = sdf.format(Date())
                binding.tvTanggal.text = currentDate

                //Deskripsi
                val description = binding.etDeskripsi.text.toString()

                //get UID
                val userID = mAuth.currentUser?.uid.toString()

                postLapor(category,currentDate,description,userID,latitude,longitude)
            }
        }
    }

    private fun postLapor(category: String, currentDate: String, description: String, userID: String, latitude: String, longitude: String) {
        val reports = Reports(category,currentDate,description,userID,latitude,longitude)
        val client = APIConfig.getAPIService().postReports(reports)
        client.enqueue(object : Callback<Reports>{
            override fun onFailure(call: Call<Reports>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<Reports>, response: Response<Reports>) {
                Toast.makeText(requireContext(), "status = ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}