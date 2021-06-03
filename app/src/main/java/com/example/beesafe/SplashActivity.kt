package com.example.beesafe

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.beesafe.databinding.ActivitySplashBinding
import com.example.beesafe.ui.OnBoardActivity
import com.example.beesafe.ui.auth.LoginActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstTime : SharedPreferences = getSharedPreferences("ONBOARD", MODE_PRIVATE)
        val firstStart: Boolean = firstTime.getBoolean("FirstTime", true)

        if (ContextCompat.checkSelfPermission(this@SplashActivity, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@SplashActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@SplashActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@SplashActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }else{
            val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            binding.splashText.startAnimation(fadein)

            Handler(Looper.getMainLooper()).postDelayed({
                if(firstTime.getBoolean("FirstTime",true)){
                    firstTime.edit().putBoolean("FirsTime", false).commit()
                    val intent = Intent(this, OnBoardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 3000)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val firstTime : SharedPreferences = getSharedPreferences("ONBOARD", MODE_PRIVATE)
        val firstStart: Boolean = firstTime.getBoolean("FirstTime", true)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@SplashActivity, Manifest.permission.ACCESS_FINE_LOCATION) ===  PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                        binding.splashText.startAnimation(fadein)

                        Handler(Looper.getMainLooper()).postDelayed({
                            if(firstStart){
                                firstTime.edit().putBoolean("FirsTime", false).commit()
                                val intent = Intent(this, OnBoardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }, 3000)
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}