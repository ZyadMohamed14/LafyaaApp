package com.example.ecommerceapp.features.dashboard.account.order.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.PriceTextStyle
import com.example.ecommerceapp.core.theme.TitleTextStyle
import com.example.ecommerceapp.core.theme.productTitleStyle
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order
import com.example.ecommerceapp.features.dashboard.cart.ui.components.PriceItem

@Composable
fun OrderDetails(order: Order){
    Column(
        modifier = Modifier
            .padding(16.dp)

    ){
        Text(text = "Product", style = TitleTextStyle)
        Spacer(modifier = Modifier.height(8.dp))
        // product section
        order.products.forEach { product ->
            Log.d("OrderDetailsScreen", "Product: ${product.name}")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, bottom = 24.dp)
                    .border(2.dp, ColorsManager.NeutralLight)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = product.image,
                        contentDescription = "Product Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .padding(8.dp)
                            .background(Color.White)
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f) // Ensure the column has enough space
                    ) {
                        Text(
                            text = product.name, // Add a fallback
                            style = productTitleStyle.copy(fontSize = 12.sp),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Quantity: ${product.quantity}", // Add a fallback
                            style = productTitleStyle.copy(fontSize = 12.sp),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "$${product.price}",
                            style = PriceTextStyle.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            ),
                        )
                    }
                }


            }
        }
        // shipping  details section
        Text(text = "Shipping Details", style = TitleTextStyle)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, ColorsManager.NeutralLight)
        ) {
            Column (
                modifier = Modifier
                    .padding(16.dp)
            ){
                ShippingDetailsitem(title = "Date Shipping", value = order.createdAt)
                Spacer(modifier = Modifier.height(20.dp))
                ShippingDetailsitem(
                    title = "Customer Name",
                    value = order.customerData.customerName
                )
                Spacer(modifier = Modifier.height(20.dp))
                ShippingDetailsitem(
                    title = "Phone",
                    value = order.customerData.customerPhone
                )
                Spacer(modifier = Modifier.height(20.dp))
                ShippingDetailsitem(title = "Address", value = order.customerData.address)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        // payment details section
        val noOfItems = order.products.size
        val items = if(noOfItems > 1) " ${noOfItems} items purchased" else "${noOfItems} item purchased"
        val itemsPrice = order.products.sumOf { it.price }
        val shippingPrice = 40.0
        Text(text = "Payment Details ", style = TitleTextStyle)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, ColorsManager.NeutralLight)
        ) {
            Column(modifier = Modifier
                .padding(16.dp)) {
                PriceItem(title = items, price = itemsPrice.toString())
                Spacer(modifier = Modifier.height(20.dp))
                PriceItem(title = "Shipping", price = shippingPrice.toString())
                Spacer(modifier = Modifier.height(20.dp))
                PriceItem(
                    title = "Total Price",
                    price = order.price.toString(),
                    textStyle = TitleTextStyle.copy(fontSize = 12.sp),
                    priceStyle = PriceTextStyle.copy(fontSize = 12.sp)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}