package com.example.beesafe.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.beesafe.model.User

class SharedPref(context: Context) {

    private var constant = Constant()
    private var sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(constant.session, constant.mode)
    }

    fun saveUser(email: String, noHp: String) {
        val editor = sharedPref.edit()
        editor.putString(constant.sessionEmail, email)
        editor.putString(constant.sessionNoHp, noHp)
        editor.putBoolean(constant.session_isLogin, true)
        editor.apply()
    }

    fun getUser(): User {
        val emailUser = sharedPref.getString(constant.sessionEmail, "")
        val noHp = sharedPref.getString(constant.sessionNoHp, "")
        return User(emailUser, noHp)
    }

    fun setLocation (latitude : Float, longitude : Float){
        val editor = sharedPref.edit()
        editor.putFloat(constant.latitude, latitude)
        editor.putFloat(constant.longitude, longitude)
        editor.apply()
    }

    fun getLatitude() : Float{
        return sharedPref.getFloat(constant.latitude,0.0F)
    }

    fun getLongitude() = sharedPref.getFloat(constant.longitude, 0.0F)

    fun setAddress(address : String){
        val editor = sharedPref.edit()
        editor.putString(constant.address, address)
        editor.apply()
    }

    fun getAddress() = sharedPref.getString(constant.address, "")

    fun clearUser() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}