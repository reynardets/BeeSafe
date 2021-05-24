package com.example.beesafe.ui.lapor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.beesafe.R

class LaporFragment : Fragment() {

    private lateinit var laporViewModel: LaporViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        laporViewModel = ViewModelProvider(this).get(LaporViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_lapor, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        laporViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}