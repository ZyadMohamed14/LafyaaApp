package com.example.ecommerceapp.features.dashboard.account.address.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "addresses")
@Parcelize
data class Address(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var userFullName: String="",
    var phone: String="",
    var details: String="",
    var city: String="",
    var isSelect: Boolean=false
): Parcelable {
    override fun toString(): String {
       return "$userFullName - $phone - $details - $city $isSelect"
    }
}