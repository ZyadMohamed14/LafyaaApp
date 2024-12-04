package com.example.ecommerceapp.features.dashboard.cart.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.PriceTextStyle
import com.example.ecommerceapp.core.theme.TitleTextStyle
import com.example.ecommerceapp.features.dashboard.cart.ui.CartViewModel

@Composable
fun PriceSection(cartViewModel: CartViewModel) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = ColorsManager.NeutralLight
            )
            .padding(8.dp)
    ) {
        val noOfItems by cartViewModel.productsState.collectAsState()
        var items = "Items (${noOfItems.size}) "

        val itemsPrice by cartViewModel.totalPrice.collectAsState()
        val shippingPrice = 10.0
        val totalPrice = itemsPrice + shippingPrice

      Column {
          PriceItem(title = items, price = itemsPrice.toString(),)
          Spacer(modifier = Modifier.height(20.dp))
          PriceItem(title = "Shipping", price = shippingPrice.toString(),)
          Spacer(modifier = Modifier.height(20.dp))
          PriceItem(title = "Total Price", price = totalPrice.toString(), textStyle = TitleTextStyle.copy(fontSize = 12.sp) , priceStyle = PriceTextStyle.copy(fontSize = 12.sp))
          Spacer(modifier = Modifier.height(20.dp))
      }
    }
}

@Composable
fun PriceItem(
    title: String,
    price: String,
    textStyle: TextStyle=BodyTextNormalRegular,
    priceStyle: TextStyle=BodyTextNormalRegular
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, style = textStyle,)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = price, style = priceStyle)
    }
}