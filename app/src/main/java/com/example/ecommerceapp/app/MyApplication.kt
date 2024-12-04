package com.example.ecommerceapp.app

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.example.ecommerceapp.BuildConfig
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.schedulers.Schedulers
@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        listenToNetworkConnectivity()
        PaymentConfiguration.init(applicationContext, "pk_test_51PwrMJF8IpAJ1lBRUdSxS3ZlKve0qQ5tgO1atk5aU8T2TFwbw2nmMWGUG0to1I78kYOekjQv1UKB2kMjSBAPKtwx005gxO00er")

    }

    @SuppressLint("CheckResult")
    fun listenToNetworkConnectivity() {
        ReactiveNetwork.observeInternetConnectivity().subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io()).subscribe { isConnected: Boolean ->
                Log.d(TAG, "Connected to internet: $isConnected")
                FirebaseCrashlytics.getInstance().setCustomKey("connected_to_internet", isConnected)
            }
    }
    companion object {
        private const val TAG = "MyApplication"
    }
}
