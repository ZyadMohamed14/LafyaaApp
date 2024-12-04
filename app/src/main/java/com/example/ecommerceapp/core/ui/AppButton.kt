package com.example.ecommerceapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.Poppins

@Composable
fun AppButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(57.dp)
            .padding(horizontal = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ColorsManager.PrimaryColor),
        shape = RectangleShape
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = Color.White,
                fontFamily = Poppins,       // Poppins font family
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
    }
}
@Composable
fun SocialButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                color = Color.White,
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White // Keeps the background solid color applied through `Modifier.background`
        )


    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp),

        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.Gray, // neutral grey for text color
            fontFamily = Poppins,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
