package com.example.ecommerceapp.features.dashboard.home.data.products

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ecommerceapp.features.detailsproduct.domain.ProductOrder

@Dao
interface ProductDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productOrder: ProductOrder)

    @Update
    suspend fun updateProduct(productOrder: ProductOrder)

    @Delete
    suspend fun deleteProduct(productOrder: ProductOrder)

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductOrder>


}