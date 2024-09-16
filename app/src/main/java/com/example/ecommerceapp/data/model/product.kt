package com.example.ecommerceapp.data.model

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ProductModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,

    @get:PropertyName("categeories_ids")
    @set:PropertyName("categeories_ids")
    var categoriesIDs: List<String>? = null,

    var images: List<String>? = null,

    var price: Int? = null,
    var rate: Float? = null,

    @get:PropertyName("sale_percentage")
    @set:PropertyName("sale_percentage")
    var salePercentage: Int? = null,

    @get:PropertyName("sale_type")
    @set:PropertyName("sale_type")
    var saleType: String? = null,

    var colors: List<String>? = null
) : Parcelable

enum class ProductSaleType(val type: String) {
    FLASH_SALE("flash_sale"),
    MEGA_SALE("mega_sale")
}