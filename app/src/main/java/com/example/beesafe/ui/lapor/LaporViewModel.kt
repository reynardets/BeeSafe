package com.example.beesafe.ui.lapor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LaporViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Lapor Fragment"
    }
    val text: LiveData<String> = _text
}