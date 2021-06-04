package com.example.beesafe.ui.akun

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beesafe.databinding.ActivityHistoryReportsBinding
import com.google.firebase.auth.FirebaseAuth

class HistoryReportsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHistoryReportsBinding
    private lateinit var adapter : HistoryReportsAdapter
    private lateinit var viewModel : HistoryReportsViewModel
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        config()
        showReports()
    }

    private fun config(){
        adapter = HistoryReportsAdapter(this)
        adapter.notifyDataSetChanged()
        mAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HistoryReportsViewModel::class.java)
    }

    private fun showReports() {
        viewModel.setReports(mAuth.currentUser?.uid.toString())
        viewModel.getReports().observe(this) { reports ->
            if(reports != null){
                adapter.setData(reports)
            }
        }
        binding.reportHistoryRv.layoutManager = LinearLayoutManager(this)
        binding.reportHistoryRv.adapter = adapter
    }
}