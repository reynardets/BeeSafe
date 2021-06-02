package com.example.beesafe.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.beesafe.R
import com.example.beesafe.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        config()
        binding.btnRegister.setOnClickListener(this)
        binding.btnLoginRegister.setOnClickListener(this)
    }

    private fun config() {
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                val email = binding.etEmailRegister.text.toString()
                val noHp = binding.etNohpRegister.text.toString()
                val pass = binding.etPasswordRegistrasi.text.toString()
                if (email.isEmpty() || noHp.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else {
                    mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(this, "User Gagal Dibuat", Toast.LENGTH_SHORT).show()
                                return@OnCompleteListener
                            } else {
                                val newUser = hashMapOf(
                                    "email" to email,
                                    "nohp" to noHp,
                                    "password" to pass
                                )
                                db.collection("users").add(newUser)
                                    .addOnSuccessListener {
                                        Log.i(
                                            "register",
                                            "Registrasi berhasil, tersimpan dengan ${it.id}"
                                        )
                                    }
                                    .addOnFailureListener {
                                        Log.e("register", "Registrasi gagal, ${it.message}")
                                    }
                                Toast.makeText(this, "User Berhasil Dibuat", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        })
                        .addOnFailureListener { exception ->
                            Log.e(
                                "Error",
                                exception.message.toString()
                            )
                        }
                }
            }
            R.id.btn_login_register -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}