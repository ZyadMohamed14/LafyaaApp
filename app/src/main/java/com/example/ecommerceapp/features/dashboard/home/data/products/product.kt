package com.example.ecommerceapp.features.dashboard.home.data.products

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
@Parcelize
data class ProductsResponse( // for all products
    val code: Int,
    val data: List<ProductModel>,
    val message: String,
    val lastVisible: String?=null
):Parcelable

@Parcelize
data class ProductResponse( // for single product
    val code: Int,
    val data: ProductModel,
    val message: String
):Parcelable
@Parcelize
@Keep
data class ProductModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var category:String? = null,
    var images: List<String>? = null,
    var image :String? = null,
    var price: Double? = null, // Changed to Double to match your product price type
    var rate: Float? = null,

    @get:PropertyName("sale_percentage")
    @set:PropertyName("sale_percentage")
    @SerializedName("sale_percentage")
    var salePercentage: Int? = null,

    @get:PropertyName("sale_type")
    @set:PropertyName("sale_type")
    var saleType: String? = null,

    var colors: List<String>? = null, // this is used if the product have only colors and not sizes

    var sizes: Map<String, Size>? = null // Map of sizes to Size class containing color and stock information
) : Parcelable

@Parcelize
data class Stock(
    val inStock: Int = 0,
    val colorKey: String = ""
) : Parcelable

@Parcelize
data class Size(
    val colors: List<Stock> = emptyList() // Default to an empty list
) : Parcelable

enum class ProductSaleType(val type: String) {
    FLASH_SALE("flash_sale"),
    MEGA_SALE("mega_sale")
}