package com.example.ecommerceapp.features.detailsproduct.screen.compontents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.ecommerceapp.core.theme.NeutralLight
import com.example.ecommerceapp.core.theme.PrimaryYellow

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..5) {

            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.Star
            val iconTintColor = if (isSelected) PrimaryYellow else NeutralLight
            Icon(
                imageVector = icon,
                contentDescription = "Star $i",
                tint = iconTintColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onRatingChanged(i.toFloat()) } // Handle user interaction
            )
        }
    }
}

