package com.example.ecommerceapp.data.reposotiry.auth.country

import com.example.ecommerceapp.data.model.auth.CountryModel
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getCountries(): Flow<List<CountryModel>>
}