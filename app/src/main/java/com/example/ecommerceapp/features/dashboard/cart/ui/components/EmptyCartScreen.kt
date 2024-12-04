package com.example.ecommerceapp.features.dashboard.cart.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.Poppins

@Composable
fun EmptyCartScreen(

) {
    // Load and animate the Lottie composition
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cart))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever // Loop animation indefinitely
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Lottie animation instead of static image

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your cart is empty ",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Poppins,
                color = ColorsManager.Gray400
            )
        )


    }
}
