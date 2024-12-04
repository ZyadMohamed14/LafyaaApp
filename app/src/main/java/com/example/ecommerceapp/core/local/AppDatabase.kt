package com.example.ecommerceapp.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ecommerceapp.features.dashboard.account.address.data.AddressDao
import com.example.ecommerceapp.features.dashboard.account.address.domain.Address

import com.example.ecommerceapp.features.dashboard.home.data.products.ProductDao
import com.example.ecommerceapp.features.detailsproduct.domain.ProductOrder
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import com.example.ecommerceapp.features.user.data.UserDao
import com.example.ecommerceapp.features.user.domain.UserDetailsModel


@Database(entities = [ProductOrder::class, UserDetailsModel::class, Address::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun addressDao(): AddressDao


}