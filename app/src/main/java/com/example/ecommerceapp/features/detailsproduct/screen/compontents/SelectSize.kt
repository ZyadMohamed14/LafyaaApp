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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.ecommerceapp.core.theme.NeuturalDark
import com.example.ecommerceapp.core.theme.sizeProductStyle
import com.example.ecommerceapp.features.dashboard.home.data.products.Stock
import com.example.ecommerceapp.features.dashboard.home.data.products.Size


@Composable
fun SelectSizes(
    sizes: Map<String, Size>,  // Pass the sizes map from ProductUIModel
    onSizeSelected: (String) -> Unit, // Callback for when a size is selected
    onSizeListSelected: (List<Stock>) -> Unit  // Callback when a size is selected to pass the colors
) {
    var selectedIndex by remember { mutableStateOf(0) } // Track selected index
    val sizeKeys = sizes.keys.toList() // List of size keys (e.g., "36", "37", etc.)
    LaunchedEffect(selectedIndex) {
        if (sizeKeys.isNotEmpty()) {
            val defaultSizeKey = sizeKeys[selectedIndex]
            val selectedSize = sizes[defaultSizeKey]
            onSizeSelected(defaultSizeKey) // Call the callback with the default selected size key
            selectedSize?.let {
                onSizeListSelected(it.colors) // Pass the default selected size's colors
            }
        }
    }

    Text(
        text = "Available Sizes",
        color = NeuturalDark,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
    ) {
        sizeKeys.forEachIndexed { index, sizeKey ->
            CircularTextItem(
                text = sizeKey,
                isSelected = index == selectedIndex,
                onSelect = {
                    selectedIndex = index
                    val selectedSize = sizes[sizeKey]
                    selectedSize?.let {
                        onSizeListSelected(it.colors) // Pass the selected size's colors
                        onSizeSelected(sizeKey) // Call the callback with the selected size key
                    }
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun CircularTextItem(
    text: String,
    backgroundColor: Color = Color.White,
    isSelected: Boolean,
    onSelect: () -> Unit,

    ) {
    val borderColor = if (isSelected) Color(0xFF29B6F6) else Color.Transparent
    Box(
        contentAlignment = Alignment.Center, // Center the text inside the circle

        modifier = Modifier
            .size(60.dp)  // Define the size of the circle
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .border(width = 3.dp, color = borderColor, shape = CircleShape)
            .clickable { onSelect() }
    ) {
        Text(
            text = text,
            // Adjust font size as needed
            style = sizeProductStyle // You can customize the text style further
        )
    }
}

@Composable
fun CircularTextList(items: List<String>) {
    var selectedIndex by remember { mutableStateOf(-1) } // Track selected index

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEachIndexed { index, item ->
            CircularTextItem(
                text = item,
                isSelected = index == selectedIndex, // Check if this item is selected
                onSelect = { selectedIndex = index } // Update selected index on click
            )
        }
    }
}
