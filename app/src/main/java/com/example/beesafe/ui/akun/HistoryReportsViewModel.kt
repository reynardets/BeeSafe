package com.example.beesafe.ui.akun

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beesafe.api.APIConfig
import com.example.beesafe.model.remote.APIResponse
import com.example.beesafe.model.remote.ReportsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryReportsViewModel : ViewModel(){

    private var listReports = MutableLiveData<ArrayList<ReportsResponse>>()

    fun setReports(userID : String){
        APIConfig.getAPIService().getUserHistoryReports(userID)
            .enqueue(object : Callback<APIResponse>{
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Log.d("Error", t.message.toString())
                }
                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    listReports.postValue(
                        response.body()?.data
                    )
                }
            })
    }

    fun getReports() = listReports
}