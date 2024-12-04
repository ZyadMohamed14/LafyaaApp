package com.example.ecommerceapp.core.local

import android.content.Context
import com.example.ecommerceapp.features.user.domain.UserDetailsModel

import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
   fun saveUserId(userId: String){
       val editor = sharedPreferences.edit()
       editor.putString("user_id", userId)
       editor.apply()
   }
    fun getUserId(): String? = sharedPreferences.getString("user_id", null)
   fun isUserIdSaved(): Boolean {
       return sharedPreferences.getBoolean("user_id_saved", false)
   }

    fun saveRoute(route: String){
        clearRoute()
        val editor = sharedPreferences.edit()
        editor.putString("route_saved", route)
        editor.apply()
    }
    fun getRoute(): String? {
        return sharedPreferences.getString("route_saved", null)
    }
    private fun clearRoute() {
        val editor = sharedPreferences.edit()
        editor.remove("route_saved")
        editor.apply()
    }
    fun saveUserDetails(userDetails: UserDetailsModel?) {
        val editor = sharedPreferences.edit()
        val jsonString = gson.toJson(userDetails)
        editor.putString("user_details", jsonString)
        editor.apply() // or editor.commit()
    }
    fun getUserDetails(): UserDetailsModel? {
        val jsonString = sharedPreferences.getString("user_details", null)
        return jsonString?.let {
            gson.fromJson(it, UserDetailsModel::class.java)
        }
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("user_email", null)
    }
    fun saveUserEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.apply()
    }


    fun clearUserDetails() {
        val editor = sharedPreferences.edit()
        editor.remove("user_details")
        editor.apply() // or editor.commit()
    }
}
