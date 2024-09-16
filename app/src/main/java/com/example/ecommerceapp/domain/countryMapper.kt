package com.example.ecommerceapp.domain

import com.example.ecommerceapp.data.model.auth.CountryModel
import com.example.ecommerceapp.ui.auth.country.model.CountryUIModel

fun CountryModel.toUIModel(): CountryUIModel {
    return CountryUIModel(
        id = id,
        name = name,
        code = code,
        currency = currency,
        image = image,
        currencySymbol = currencySymbol
    )
}