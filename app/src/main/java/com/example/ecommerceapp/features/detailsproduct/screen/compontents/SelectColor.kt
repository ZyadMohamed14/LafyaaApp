package com.example.ecommerceapp.features.detailsproduct.screen.compontents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.NeuturalDark
import com.example.ecommerceapp.features.dashboard.home.data.products.Stock


@Composable
fun SelectColors(
    colorsStock: List<Stock> = emptyList(),
    colors: List<String> = emptyList(),
    onColorSelected: (String) -> Unit

    ) {
    Text(
        text = "Available Color",
        color = NeuturalDark,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    var colorList = remember {
        mutableStateOf(emptyList<Color>())
    }
    if(colorsStock.isNotEmpty()){
        // for products that has sizes like shoes
        colorList.value =colorsStock.map { Color(android.graphics.Color.parseColor(it.colorKey)) } // Convert color keys to Color
    }else{
        // any product that has no sizes
        colorList.value =colors.map { Color(android.graphics.Color.parseColor(it)) } // Convert color keys to Color
    }


    CircularColorsList(colorList.value,onColorSelected)
}
@Composable
fun CircularColorItem(

    backgroundColor: Color,
    isSelected: Boolean,
    onSelect: () -> Unit,

    ) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(60.dp)  // Define the size of the circle
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .border(2.dp,ColorsManager.NeutralLight,shape = CircleShape)
            .clickable { onSelect() }
    ){
        if(isSelected){
            Box(

                modifier = Modifier
                    .size(25.dp)  // Define the size of the circle
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )

            )
        }

    }


}

@Composable
fun CircularColorsList(items: List<Color>,onColorSelected: (String) -> Unit,) {
    var selectedIndex by remember { mutableStateOf(-1) } // Track selected index

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(
                rememberScrollState()
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEachIndexed { index, item ->
            CircularColorItem(
                backgroundColor = item,
                isSelected = index == selectedIndex, // Check if this item is selected
                onSelect = {
                    onColorSelected(item.toString())
                    selectedIndex = index } // Update selected index on click
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
