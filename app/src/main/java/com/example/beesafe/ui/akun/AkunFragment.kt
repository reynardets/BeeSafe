package com.example.beesafe.ui.akun

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beesafe.R
import com.example.beesafe.databinding.FragmentAkunBinding
import com.example.beesafe.ui.auth.LoginActivity
import com.example.beesafe.utils.SharedPref

class AkunFragment : Fragment(), View.OnClickListener {

    private lateinit var akunViewModel: AkunViewModel
    private lateinit var binding: FragmentAkunBinding
    private lateinit var pref: SharedPref

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAkunBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
        binding.btnLogout.setOnClickListener(this)
    }

    private fun config() {
        akunViewModel = ViewModelProvider(this).get(AkunViewModel::class.java)
        pref = SharedPref(requireContext())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_logout -> {
                pref.clearUser()
                startActivity(Intent(view?.context, LoginActivity::class.java))
                activity?.finish()
            }
        }
    }
}