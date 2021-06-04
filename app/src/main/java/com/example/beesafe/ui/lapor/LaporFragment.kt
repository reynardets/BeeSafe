package com.example.beesafe.ui.lapor

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.beesafe.R
import com.example.beesafe.api.APIConfig
import com.example.beesafe.databinding.FragmentLaporBinding
import com.example.beesafe.model.Reports
import com.example.beesafe.utils.SharedPref
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class LaporFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentLaporBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var pref: SharedPref
    private lateinit var mAuth: FirebaseAuth
    private lateinit var laporViewModel: LaporViewModel
    private var latitude = ""
    private var longitude = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLaporBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
        binding.btnLapor.setOnClickListener(this)
        binding.tvTanggal.setOnClickListener(this)
        binding.etLokasi.setOnClickListener(this)
        binding.etLokasi.setMovementMethod(ScrollingMovementMethod())
        binding.etDeskripsi.setMovementMethod(ScrollingMovementMethod())
    }

    private fun config() {
        pref = SharedPref(requireContext())
        mAuth = FirebaseAuth.getInstance()
        laporViewModel = LaporViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.et_lokasi ->{
                startActivity(Intent(view?.context, LocationActivity::class.java))
        }
            R.id.tv_tanggal -> {
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val datePickerDialog = DatePickerDialog(requireContext(), R.style.DatePickerDialog, this, year, month, day)
                datePickerDialog.show()
            }
            R.id.btn_lapor -> {
                //Cek Field
                if (binding.etDeskripsi.text.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Lokasi tidak Boleh Kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (binding.tvTanggal.text == "") {
                    Toast.makeText(
                        requireContext(),
                        "Tanggal tidak Boleh Kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (binding.etDeskripsi.text.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Deskripsi kejadian tidak Boleh Kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                //get Lat-Long from SharedPref
                latitude = pref.getLatitude().toString()
                longitude = pref.getLongitude().toString()
                //Temporary Category
                val category = ""
                //get Current Date
                var currentDate = binding.tvTanggal.text.toString()
                //Deskripsi
                val description = binding.etDeskripsi.text.toString()
                //get UID
                val userID = mAuth.currentUser?.uid.toString()
                postLapor(category, currentDate, description, userID, latitude, longitude)
        }
        }
    }

    private fun postLapor(
        category: String,
        currentDate: String,
        description: String,
        userID: String,
        latitude: String,
        longitude: String
    ) {
        val reports = Reports(category, currentDate, description, latitude, longitude, userID)
        val client = APIConfig.getAPIService().postReports(reports)
        client.enqueue(object : Callback<Reports> {
            override fun onFailure(call: Call<Reports>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Reports>, response: Response<Reports>) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Laporan Berhasil Dikirim")
                builder.setIcon(R.drawable.ic_warning)
                builder.setMessage("Apabila anda memperlukan bantuan lebih, silahkan hubungi 129")
                builder.setPositiveButton("OK"){dialog, which ->  }
                val alertDialog = builder.create()
                alertDialog.show()
                binding.etDeskripsi.setText("")
            }
        })

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val monthNew = month + 1
        val date = "${dayOfMonth}/${monthNew}/${year}"
        binding.tvTanggal.text = date
    }

    override fun onResume() {
        super.onResume()
        binding.etLokasi.text = pref.getAddress()
    }

    override fun onDestroy() {
        super.onDestroy()
        pref.eraseAdress()
    }
}