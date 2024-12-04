package com.example.ecommerceapp.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.Poppins
import androidx.compose.runtime.*
import com.airbnb.lottie.compose.*
import com.example.ecommerceapp.R

@Composable
fun AppAlertDialog(
    title: String,
    message: String,
    imageResource: Int,
    buttonText: String,
    onDismiss:  () -> Unit={},
    onConfirm: () -> Unit={}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title, style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins,
                color= ColorsManager.PrimaryDarkColor
            ))
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = message, textAlign = TextAlign.Center
                ,style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Poppins,
                        color= ColorsManager.Gray400
                    )
                )
            }
        },
        confirmButton = {
            AppButton(text = buttonText) {
               onConfirm()
            }

        },

    )
}


@Composable
fun AppLoadingDialog(
    title: String = "Loading...",
    message: String = "Please wait while we process your request",
    onDismiss: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation)) // Replace with your animation file
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = ColorsManager.PrimaryDarkColor
                )
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                // Lottie animation for loading
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Poppins,
                        color = ColorsManager.Gray400
                    )
                )
            }
        },
        confirmButton = {

        }
    )
}
