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

    fun clearUser() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}