package com.example.ecommerceapp.features.detailsproduct.screen.compontents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.theme.NeturalGrey
import com.example.ecommerceapp.core.theme.PriceTextStyle
import com.example.ecommerceapp.core.theme.productTitleStyle
import com.example.ecommerceapp.features.dashboard.home.data.products.Stock
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun ProductDetials(
productUIModel: ProductUIModel,
onSizeSelected: (String) -> Unit,
onColorSelected: (String) -> Unit
) {
    var isFavoutites by remember {
        mutableStateOf(false)
    }
    var rating by remember { mutableStateOf(1f) }
    var selectedColors by remember { mutableStateOf(emptyList<Stock>()) } // Keep track of colors based on size

    Column (
        modifier = Modifier.padding(20.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically  // Centers items vertically
        ) {
            Text(
                text = if (productUIModel.name.length > 24) {
                    productUIModel.name.chunked(24).joinToString("\n")  // Inserts a line break after every 24 characters
                } else {
                    productUIModel.name
                },

                style = productTitleStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f) // Allows text to take up available space
            )

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    isFavoutites = !isFavoutites
                }
            ) {
                val icon = if (isFavoutites) R.drawable.baseline_favorite_24 else R.drawable.outline_favorite_border_24
                val tintColor = if (isFavoutites) Color.Red else NeturalGrey

                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "Favorite icon",
                    tint = tintColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        RatingBar(
            rating = productUIModel.rate,
            onRatingChanged = {
                rating = it
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = productUIModel.price.toString(),style = PriceTextStyle)
        Spacer(modifier = Modifier.height(16.dp))

        // sizes  and colors section
        if(!productUIModel.sizes.isNullOrEmpty()){
            productUIModel.sizes?.let { sizes ->
                SelectSizes(sizes = sizes,onSizeSelected = onSizeSelected) { colors ->
                    selectedColors = colors // Update colors when size is selected
                }
                SelectColors(colorsStock = selectedColors){
                    onColorSelected(it)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if(productUIModel.colors.isNotEmpty()){
            SelectColors(colors = productUIModel.colors){
                onColorSelected(it)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        ProductDescrption(productUIModel.description)


    }

}