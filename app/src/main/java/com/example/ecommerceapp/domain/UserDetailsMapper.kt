package com.example.ecommerceapp.domain

import com.example.ecommerceapp.data.model.user.CountryData
import com.example.ecommerceapp.data.model.user.UserDetailsModel
import com.example.ecommerceapp.data.model.user.UserDetailsPreferences

fun UserDetailsPreferences.toUserDetailsModel(): UserDetailsModel {
    return UserDetailsModel(
        id = id,
        email = email,
        name = name,
        reviews = reviewsList
    )
}

fun UserDetailsModel.toUserDetailsPreferences(counytryData: CountryData): UserDetailsPreferences {
    return UserDetailsPreferences.newBuilder()
        .setId(id)
        .setEmail(email)
        .setName(name)
        .addAllReviews(reviews?.toList() ?: emptyList())
     //   .setCountry(countryData)
        .build()
}