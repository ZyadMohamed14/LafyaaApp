package com.example.ecommerceapp.features.dashboard.account.order.domain

import android.os.Parcelable
import com.example.ecommerceapp.core.utils.getCurrentFormattedDate
import com.example.ecommerceapp.features.detailsproduct.domain.ProductOrder
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Order(
    val products: List<ProductOrder>,
    val customerData: CustomerData,
    val isPaid: Boolean,
    val status: String="Created",
    val price: Double,
    val orderId: String = "",
    //userId
    val userId: String="",
    val createdAt: String = Date().getCurrentFormattedDate()
) : Parcelable {
    @Override
    override fun toString(): String {
        return "Order(userId='$userId', products=$products, customerData=$customerData, " +
                "isPaid=$isPaid, status=$status, price=$price, orderId='$orderId', createdAt='$createdAt')"
    }
}

@Parcelize
data class CustomerData(
    val customerName: String,
    val customerPhone: String,
    val address: String,
) : Parcelable



data class OrderResponse(
    val code: Int,
    val data: List<Order>,
    val message: String
)
data class CreatedOrderResponse(
    val code: Int,
    val message: String,
    val data: OrderId
)

data class OrderId(
    @SerializedName("orderId") val orderId: String
)

