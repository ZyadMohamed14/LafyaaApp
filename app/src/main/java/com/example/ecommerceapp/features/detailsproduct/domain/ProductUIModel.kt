package com.example.ecommerceapp.features.detailsproduct.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ecommerceapp.features.dashboard.home.data.products.Size
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.UUID


data class ProductUIModel(
   val id: String,
    val name: String,
    val description: String,
    val category: String,
    val images: List<String>, // Non-nullable list of images
    val price: Double,        // Non-nullable price for simplicity in the UI
    val rate: Float,          // Non-nullable rating for simplicity in the UI
    val priceAfterSale: Int? = null,  // Price after applying sale (nullable)
    val salePercentage: Int?, // Nullable sale percentage
    val saleType: String?,
    var sizes: Map<String, Size>? = null,// Nullable sale type
    val colors: List<String>, // Non-nullable list of colors, can be empty
    val currencySymbol: String = "$",  // Default currency symbol (can be customized)
    val quantity: Int = 1 // for cartItem
){

    fun getFormattedPrice(): String {
        return "$currencySymbol$price"
    }

    fun getFormattedPriceAfterSale(): String {
        if (saleType == null || salePercentage == null) return getFormattedPrice()
        val newPrice = salePercentage.let { price.minus(price * it / 100) }
        return "$currencySymbol$newPrice"
    }

    fun getFormattedSale(): String {
        return "$salePercentage%"
    }

    fun getFirstImage(): String {
        return images.firstOrNull() ?: ""
    }


}


@Parcelize
@Entity(tableName = "products")
data class ProductOrder(
    @PrimaryKey val id: String = UUID.randomUUID().toString(), // Generate unique ID
    val name: String,
    val category: String,
    val image: String,
    val price: Double,
    val quantity: Int=1,
    val color: String,
    val size: String="",

    ) : Parcelable{
   override fun toString(): String {
       return "ProductOrder(id='$id', name='$name', category='$category', image='$image', price=$price, quantity=$quantity, color='$color', size='$size')"
   }
    }