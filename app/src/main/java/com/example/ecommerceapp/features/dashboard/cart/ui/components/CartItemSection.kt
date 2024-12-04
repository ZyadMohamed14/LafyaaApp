package com.example.ecommerceapp.features.dashboard.cart.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.NeturalGrey
import com.example.ecommerceapp.core.theme.PriceTextStyle
import com.example.ecommerceapp.core.theme.productTitleStyle
import com.example.ecommerceapp.core.utils.toColor
import com.example.ecommerceapp.features.dashboard.cart.ui.CartViewModel
import com.example.ecommerceapp.features.detailsproduct.domain.ProductOrder
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel

@Composable
fun CartItem(product: ProductOrder, cartViewModel: CartViewModel) {
    var isFavourites by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, bottom = 24.dp)
            .border(2.dp, ColorsManager.NeutralLight)

    ) {
        AsyncImage(
            model = product.image,
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(72.dp)
                .padding(8.dp)
                .background(Color.White)
                .align(Alignment.CenterVertically)
        )

        Column {
            // Name , Favorite and Delete section
            Row(
                modifier = Modifier.padding(4.dp)
            ) {
                // Name section
                Text(
                    text = if (product.name.length > 24) {
                        product.name.chunked(24)
                            .joinToString("\n")  // Inserts a line break after every 24 characters
                    } else {
                        product.name
                    },
                    style = productTitleStyle.copy(fontSize = 12.sp),
                    modifier = Modifier.weight(2f)
                )
                // Favorite section
                Icon(
                    painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                    contentDescription = "Favorite icon",
                    tint = if (isFavourites) Color.Red else NeturalGrey,


                    )
                // Delete section
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                    contentDescription = "delete Icon",
                    modifier = Modifier.weight(1f).clickable {
                                                             cartViewModel.removeFromCart(product)
                    },
                    tint = ColorsManager.NeutralGrey,

                    )
            }
            // Price and Quantity section
            Row(
                modifier = Modifier.padding(4.dp, bottom = 8.dp)
            ) {

                // Price section
                Text(
                    text = "$${product.price}",
                    style = PriceTextStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(2f)
                )
                // Quantity section
                Box(
                    modifier = Modifier
                        .background(ColorsManager.White, shape = RectangleShape)
                        .width(115.dp)
                        .border(
                            1.dp,
                            color = ColorsManager.NeutralLight,
                        ) // Fixed here
                        .padding(3.dp)
                ) {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.minus),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .background(color = ColorsManager.White)
                                .size(16.dp)
                                .fillMaxHeight()
                                .align(Alignment.CenterVertically)
                                .weight(1f)
                                .clickable {
                                    cartViewModel.decrementQuantity(product.id)
                                },
                        )
                        Text(
                            text = "${product.quantity}",
                            modifier = Modifier
                                .weight(2f)
                                .background(color = ColorsManager.NeutralLight),
                            textAlign = TextAlign.Center
                        )
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .background(color = ColorsManager.White)
                                .size(16.dp)
                                .fillMaxHeight()
                                .align(Alignment.CenterVertically)
                                .weight(1f)
                                .clickable {
                                    cartViewModel.incrementQuantity(product.id)
                                },
                        )
                    }

                }

            }
            //color and size section
            Row(
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = "Color: ",
                    style = BodyTextNormalRegular.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier
                    .size(16.dp)
                    .background(product.color.toColor()!!))


            }
            Text(text = "Size : ${product.size}", style = BodyTextNormalRegular.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),)

        }



    }
}