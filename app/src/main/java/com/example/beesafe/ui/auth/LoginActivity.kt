package com.example.beesafe.ui.auth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.beesafe.MainActivity
import com.example.beesafe.R
import com.example.beesafe.databinding.ActivityLoginBinding
import com.example.beesafe.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var pref: SharedPref

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        config()
        isLogin()
        binding.btnLogin.setOnClickListener(this)
        binding.txtRegister.setOnClickListener(this)

    }

    private fun config() {
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        pref = SharedPref(this)
    }

    private fun isLogin() {
        if(pref.getUser().email != ""){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val email = binding.etEmailLogin.text.toString()
                val password = binding.etPasswordLogin.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    showLoading(true)
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Sign In Berhasil", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                            onAuthSuccessful(email)
                        } else {
                            Toast.makeText(this, "Sign In Gagal", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                } else {
                    showLoading(false)
                    Toast.makeText(this, "Email dan Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.txt_Register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun onAuthSuccessful(email: String) {
        db.collection("users").whereEqualTo("email", email).get()
            .addOnSuccessListener {
                it.documents.forEach { document ->
                    val email = document["email"].toString()
                    val noHp = document["nohp"].toString()
                    pref.saveUser(email, noHp)
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Sign In Gagal, $it", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBar.playAnimation()
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}